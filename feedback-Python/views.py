from django.db import IntegrityError
from rest_framework.response import Response
from rest_framework.viewsets import ModelViewSet
from feedback.models import Feature, Release, FeedbackResult, Version, FeedbackActivity, UserSession
from feedback.serializers import FeatureSerializer, ReleaseSerializer, FeedbackResultSerializer, \
    VersionSerializer, FeedbackActivitySerializer, UserSessionSerializer


class FeatureView(ModelViewSet):
    queryset = Feature.objects.all()
    serializer_class = FeatureSerializer
    filterset_fields = ('name',)


class VersionView(ModelViewSet):
    queryset = Version.objects.all()
    serializer_class = VersionSerializer
    filterset_fields = ('tag',)


class ReleaseView(ModelViewSet):
    queryset = Release.objects.all()
    serializer_class = ReleaseSerializer
    # filterset_fields = ('version',)


class FeedbackResultView(ModelViewSet):
    queryset = FeedbackResult.objects.all()
    serializer_class = FeedbackResultSerializer


class FeedbackActivityView(ModelViewSet):
    queryset = FeedbackActivity.objects.all()
    serializer_class = FeedbackActivitySerializer
    filterset_fields = ('user_email',)


class UserSessionView(ModelViewSet):
    queryset = UserSession.objects.all()
    serializer_class = UserSessionSerializer
    filterset_fields = ('user_email', 'tag')
