from rest_framework import serializers
from feedback.models import Feature, Release, FeedbackResult, Version, FeedbackActivity, UserSession
from organizations.models import Service


class FeatureSerializer(serializers.ModelSerializer):
    class Meta:
        model = Feature
        fields = '__all__'


class VersionSerializer(serializers.ModelSerializer):
    class Meta:
        model = Version
        fields = '__all__'


class ReleaseSerializer(serializers.ModelSerializer):
    features_repr = FeatureSerializer(read_only=True, many=True, source='features')
    features = serializers.PrimaryKeyRelatedField(queryset=Feature.objects.all(), many=True)
    version_repr = VersionSerializer(read_only=True, source='version')
    version = serializers.PrimaryKeyRelatedField(queryset=Version.objects.all())

    class Meta:
        model = Release
        fields = '__all__'


class FeedbackResultSerializer(serializers.ModelSerializer):
    feature_repr = FeatureSerializer(read_only=True, source="feature")
    service_id = serializers.PrimaryKeyRelatedField(queryset=Service.objects.all(), source='service')

    class Meta:
        model = FeedbackResult
        fields = '__all__'


class FeedbackActivitySerializer(serializers.ModelSerializer):
    """
    The serialization of release_repr is required in order to extract version and environment on the frontend since
    this information is not contained in the feedback-activity objects.
    """
    release_repr = ReleaseSerializer(read_only=True, source="release")
    service_id = serializers.PrimaryKeyRelatedField(queryset=Service.objects.all(), source='service')

    class Meta:
        model = FeedbackActivity
        fields = '__all__'


class UserSessionSerializer(serializers.ModelSerializer):
    class Meta:
        model = UserSession
        fields = '__all__'
