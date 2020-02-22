import unittest

from feedback.signals import user_session_post_save
from feedback.models import UserSession


class TestUserSessionCalculation(unittest.TestCase):
    # Create mock sessions which match filtering condition

    # Based on those we can test whether test_user_session_post_save returns the desired value
    user_session1 = UserSession.objects.create(
        user_email='a@a.com',
        session_start='2020-02-19T00:00:00.000000Z',
        session_end='2020-02-19T05:00:00.000000Z',
        tag='0.0.0'
    )
    user_session2 = UserSession.objects.create(
        user_email='a@a.com',
        session_start='2020-02-20T00:00:00.000000Z',
        session_end='2020-02-20T00:00:00.000000Z',
        tag='0.0.0'
    )
    user_session3 = UserSession.objects.create(
        user_email='a@a.com',
        session_start='2020-02-21T00:00:00.000000Z',
        session_end='2020-02-21T00:00:00.000000Z',
        tag='0.0.0'
    )

    user_sessions = [user_session1, user_session2, user_session3]

    def test_user_session_post_save(self) -> float:

        # problem is that the function filters the actual database and not the mock that I've created.
        hours_used_release = user_session_post_save(sender=UserSession, instance=self.user_session3,
                                                    test_db=self.user_sessions)
        self.assertEqual(hours_used_release, 5)
