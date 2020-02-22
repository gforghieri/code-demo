from django.db import models
from django.utils.translation import ugettext_lazy as _


class Feature(models.Model):
    name = models.CharField('Feature Name', max_length=50, blank=True, unique=True)
    description = models.CharField('Feature Description', max_length=150, blank=True)
    info_link = models.CharField('Feature Demo Link', max_length=100, blank=True)

    class Meta:
        verbose_name = _('feature')
        verbose_name_plural = _('features')
        ordering = ['id']


class Version(models.Model):
    tag = models.CharField('Tag', max_length=50, unique=True)

    class Meta:
        verbose_name = _('tag')
        verbose_name_plural = _('tags')


class Release(models.Model):
    version = models.ForeignKey(Version, on_delete=models.CASCADE)
    features = models.ManyToManyField(Feature, blank=True)

    class Meta:
        verbose_name = _('release')
        verbose_name_plural = _('releases')


class FeedbackResult(models.Model):
    user_email = models.EmailField('Email', blank=False, null=False)
    service = models.ForeignKey('organizations.service', null=True, on_delete=models.SET_NULL)
    feature = models.ForeignKey(Feature, on_delete=models.CASCADE)
    feedback = models.CharField('Feature Feedback', max_length=512, blank=True, null=True)
    liked = models.NullBooleanField('Feature Liked')
    skipped = models.NullBooleanField('Feature Skipped')

    class Meta:
        verbose_name = _('feedback-result')
        verbose_name_plural = _('feedback-results')


class FeedbackActivity(models.Model):
    user_email = models.EmailField('Email', blank=False)
    declined = models.NullBooleanField('Declined', null=True, blank=True)
    release = models.ForeignKey(Release, null=True, blank=True, on_delete=models.CASCADE)
    service = models.ForeignKey('organizations.service', null=True, blank=True, on_delete=models.CASCADE)
    has_given_feedback = models.NullBooleanField('Given Feedback', blank=True)
    hours_used_release = models.FloatField(null=True, blank=True)

    class Meta:
        verbose_name = _('feedback-activity')
        verbose_name_plural = _('feedback-activities')


class UserSession(models.Model):
    user_email = models.EmailField('Email', blank=False)
    session_start = models.DateTimeField(null=True)
    session_end = models.DateTimeField(null=True)
    tag = models.CharField(null=True, max_length=30)

    class Meta:
        verbose_name = _('user-session')
        verbose_name_plural = _('user-sessions')
