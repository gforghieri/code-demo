def track_user_session(user=None, request=None):
    """Creates, filters and updates UserSessions on the core and sends UserSessions to the hub on next login

    Filter the local UserSession objects per user and get their most recent user_session object

    If its a LOGIN REQUEST and the UserSession exists, we send this UserSession to the hub
    Then FeedbackActivity model hours_used_release field is calculated on the hub
    Then we create a new UserSession on the core-service

    Else any other type of REQUEST (e.q. verify, refresh), overwrite session_end

    Args:
        user: The user objects from people.models
        request: The request object from django_rest_framework

    Returns:
        None
    """

    user_session = UserSession.objects.filter(user_email=user.email).order_by('session_start').last()

    # serialize, then transform to JSON format (.data)
    user_session_serial = UserSessionSerializer(user_session).data

    request_url = request.META.get('PATH_INFO', '')
    if '/api-token-auth/' in request_url:  # User login, new session start
        if user_session:
            hub_token = ServiceToServiceAuth().generate_hub_token()
            request_params = {
                'url': settings.HUB_URL + 'feedback/user-sessions/',
                'json': user_session_serial,
                'headers': {"Authorization": "STS-JWT {0}".format(hub_token)}
            }
            try:
                hub_response = requests.post(**request_params)
                new_user_session = UserSession.objects.create(user_email=user.email, session_start=datetime.now(),
                                                              session_end=datetime.now(),
                                                              tag=settings.VERSION)  # settings.VERSION
            except requests.ConnectionError:
                return create_error_response('Failed to connect to hub service: {url}'.format(**request_params))

    else:  # Any other request e.q. refresh, verify
        user_session.session_end = datetime.now()
        user_session.save()
