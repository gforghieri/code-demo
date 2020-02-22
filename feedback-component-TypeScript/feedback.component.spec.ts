import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {NO_ERRORS_SCHEMA} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from '@angular/material';
import {FeedbackComponent} from './feedback.component';
import {FeedbackResultService} from '../services/data/feedback-result.service';
import {FeedbackServiceStub} from '../services/data/feedback.service.stub';
import {MatDialogrefStub} from './mat.dialogref.stub';
import {Router} from '@angular/router';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {ParsedToken, TokenService} from '@utility/utility';
import {DialogService, RouterStub} from 'dropboard-framework';
import {FeedbackActivityService} from '../services/data/feedback-activity.service';

describe('FeedbackComponent', () => {
    let component: FeedbackComponent;
    let fixture: ComponentFixture<FeedbackComponent>;

    const releaseMock = {
        id: 1,
        environment: null,
        feature_repr: [
            {
                name: 'Feature X',
                description: null,
                link: null,
                liked: null,
                skipped: null,
                feedback: null,
            },
            {
                name: 'Feature Y',
                description: null,
                link: null,
                liked: null,
                skipped: null,
                feedback: null,
            }
        ]
    };


    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [FeedbackComponent],
            schemas: [NO_ERRORS_SCHEMA],
            imports: [NoopAnimationsModule, MatDialogModule],
            providers: [
                DialogService,
                {provide: FeedbackResultService, useClass: FeedbackServiceStub},
                {provide: FeedbackActivityService, useClass: FeedbackServiceStub},
                {provide: MatDialogRef, useClass: MatDialogrefStub},
                {provide: Router, useClass: RouterStub},
                {provide: MAT_DIALOG_DATA, useValue: releaseMock},
            ]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(FeedbackComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    /**
     * Testing constructor() and ngOnInit()
     */
    test('R: should create feedback component', () => {
        expect(component).toBeTruthy();
    });


    test('R: expect that the releaseMock and features are set through the MAT_DIALOG_DATA', () => {
        expect(component.release).toEqual(releaseMock);
        expect(component.features).toEqual(releaseMock.feature_repr);
    });

    /**
     * Testing onSubmit()
     */
    test('R: Should save the expected feedback-activity and feedback-result', () => {
        const tokenMock = {'email': 'a@a.com'};
        jest.spyOn(TokenService, 'getParsedToken').mockImplementation(() => tokenMock as ParsedToken);
        jest.spyOn(TestBed.get(FeedbackActivityService), 'saveFeedbackActivity');
        jest.spyOn(TestBed.get(FeedbackResultService), 'saveFeedbackResult');

        component.onSubmit();

        const expectedFeedbackActivity = {
            user: tokenMock.email,
            release: releaseMock.id,
            declined: false,
            has_given_feedback: true
        };

        expect(TestBed.get(FeedbackActivityService).saveFeedbackActivity).toHaveBeenCalledWith(expectedFeedbackActivity);
        expect(TestBed.get(FeedbackResultService).saveFeedbackResult).toHaveBeenCalled();
    });

    /**
     * Testing hideInstructions()
     */
    test('R: Should make isFirstPage false', () => {
        component.hideInstructions();
        expect(component.isFirstPage).toBe(false);
    })
});
