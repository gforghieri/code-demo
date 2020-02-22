from django.apps import AppConfig
from django.db.models.signals import post_save


class FeedbackConfig(AppConfig):
    name = 'feedback'

    def ready(self):
        # noinspection PyUnresolvedReferences
        from . import signals
