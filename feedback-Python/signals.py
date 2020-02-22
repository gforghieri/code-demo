from django.db.models.signals import post_save
from django.dispatch import receiver
from feedback.models import UserSession
from feedback.models import FeedbackActivity


@receiver(post_save, sender=UserSession)
def user_session_post_save(sender, instance, test_db, *args, **kwargs, ) -> float:
    """
    After a UserSession has been saved, we filter it and aggregate its timestamp to know how long a user used a certain
    release. Filter on the tag e.q. (0.0.0) which we drop the last char of so (0.0.) since we do not consider smaller
    updates different releases. Then the user_email has to match. Then each remaining entry session_end - session_start
    and sum them in FeedbackActivity's hours_used_release field where the tag matches.

    Objective: To populate the hours_used_release field of the FeedbackActivity model which has a front-end object
    representation used to prompt the bottomsheet.component when the user has used a certain release for X.YZ hours.

    """
    stripped_tag = instance.tag[:-1]

    if test_db:
        user_sessions = test_db
    else:
        UserSession.objects.all().filter(tag__startswith=stripped_tag).filter(user_email=instance.user_email)

    seconds_used_release = 0
    for user_session in user_sessions:
        timedelta_session = user_session.session_end - user_session.session_start
        seconds_per_session = timedelta_session.total_seconds()
        seconds_used_release += seconds_per_session

    minutes_used_release = seconds_used_release / 60
    hours_used_release = minutes_used_release / 60

    # Populate the hours_used_release of the FeedbackActivity model for a specific user and version
    # 1. Filter first
    feedback_activities = FeedbackActivity.objects.all().filter(release__version__tag__startswith='0.0.') \
        .filter(user_email=instance.user_email)
    # TODO: in PROD the above filter should change to 'stripped_tag' instead of '0.0.' in dev 'stripped_tag' is 'loca'
    #  In dev env, the version from the front-end client is '0.0.0', the version in the cs shows 'local', not matching

    # 2. Populate
    for feedback_activity in feedback_activities:
        feedback_activity.hours_used_release = hours_used_release
        feedback_activity.save()

    return hours_used_release
