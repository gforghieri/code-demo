import {Component, Inject, OnInit, ViewEncapsulation} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {TokenService} from '@utility/utility';
import {Feature, Release} from '../services/data/release.service';
import {animate, style, transition, trigger} from '@angular/animations';
import {FeedbackResultService} from '../services/data/feedback-result.service';
import {FeedbackActivity, FeedbackActivityService} from '../services/data/feedback-activity.service';
import {Router} from '@angular/router';
import {ConfirmDialogComponent, DialogService} from 'dropboard-framework';
import {HubOrganisationsService, Service} from '../services/data/hub-organisations.service';
import * as _ from 'lodash';

// We use this feedbackArray just as a frontend object in the feedback HTML ng-for loop.
interface UserFeedback {
    feature: Feature;
    liked: boolean;
    skipped: boolean;
    feedback: string;
    dirty: boolean;
}

@Component({
    selector: 'feedback-component-dialog',
    templateUrl: 'feedback.component.html',
    styleUrls: ['feedback.component.scss'],
    encapsulation: ViewEncapsulation.None,
    animations: [
        trigger('InsertTrigger', [
            transition(':enter', [
                style({height: 0, opacity: 0}),
                animate('225ms cubic-bezier(0.4,0.0,1,1)', style({height: '*', opacity: 1})),
            ])
        ]),
        trigger('RemoveTrigger', [
            transition(':leave', [
                style({height: '*', opacity: 1}),
                animate('195ms cubic-bezier(0.0,0.0,0.2,1)', style({height: 0, opacity: 0})),
            ])
        ])
    ]

})
/**
 * @date: created on 08-07-2019
 * @author gforghieri
 * @author gmforghieri
 * This class is responsible for the front-end functionality of the feedbackform-component.
 */
export class FeedbackComponent implements OnInit {
    private _release_repr: Release;
    private _features: Feature[];
    private _userFeedbackArray: UserFeedback[] = [];
    private _isFirstPage = true;
    private _isLastPage = false;
    private _service: Service;
    private _feedbackActivity: FeedbackActivity;

    constructor(
        private _dialogService: DialogService,
        private _feedbackResultService: FeedbackResultService,
        private _feedbackActivityService: FeedbackActivityService,
        private _hubOrganisationService: HubOrganisationsService,
        private _dialogRef: MatDialogRef<FeedbackComponent>,
        private _router: Router,
        // Inject the data sent from the server through the Matdialog component, Dependency injection to feedback dialog
        @Inject(MAT_DIALOG_DATA) public data: any) {
        this._release_repr = data[1];
        this._feedbackActivity = data[2];
        this._features = this._release_repr.features_repr;
        // Initialize the values to populate the ngfor loop in the feedback HTML.
        this._userFeedbackArray = _.map(this._features, feature => {
            return {
                feature: feature,
                liked: null,
                skipped: null,
                feedback: null,
                dirty: false
            }
        });
    }

    ngOnInit() {
        // Subscribe to any router events, when that happens close down the dialog.
        this._router.events.subscribe(() => this._dialogRef.close());
        this._hubOrganisationService.getService().subscribe(service => this._service = service)
    }

    /**
     * Upon clicking the onSubmit button on the feedback form,
     * the feedback-activity and feedback result array is sent to the server containing
     * the username, that they've given feedback and the content of their feedback.
     */
    onSubmit(): void {
        this._isLastPage = true;
        // Set FeedbackActivity values
        this._feedbackActivity.user_email = TokenService.getParsedToken().email;
        this._feedbackActivity.declined = false;
        this._feedbackActivity.release_repr = this._release_repr;
        // this._feedbackActivities[0].release_id = this._release_repr.id;
        this._feedbackActivity.service_id = this._service.id;
        this._feedbackActivity.has_given_feedback = true;
        this._feedbackActivity.save();

        // Add individual feedback result objects to feedback results array
        for (const feedback of this._userFeedbackArray) {
            this._feedbackResultService.create({
                user_email: TokenService.getParsedToken().email,
                service_id: this._service.id,
                feature: feedback.feature.id,
                liked: feedback.liked,
                skipped: feedback.skipped,
                feedback: feedback.feedback
            }).save()
        }
    };

    /**
     * Opens a link to demo the feature.
     * @param url
     */
    featureDemoLink(url: String): void {
        // @ts-ignore
        window.open(url, '_blank');
    }


    hideInstructions(): void {
        this._isFirstPage = false;
    }

    /**
     * Show the instructions again if the user goes to the first page
     * by clicking the previous button on the second page.
     * Otherwise make sure it is removed from the DOM.
     */
    showInstructions(index: number): void {
        // if index equals 1, true, else false
        this._isFirstPage = index === 1;
    }

    closeModal(): void {
        this._dialogRef.close();
    }

    closeConfirmModal(): void {
        this._dialogService.open(ConfirmDialogComponent, {
            title: 'Close the feedback form without completing?',
            confirmButtonName: 'YES',
            cancelButtonName: 'NO'
            //
        }).subscribe(dialogRef => dialogRef.afterClosed().subscribe(confirm => confirm && this.closeModal()));
    }
}







