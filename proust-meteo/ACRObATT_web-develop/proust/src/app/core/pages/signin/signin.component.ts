import {Component, OnInit, NgModule} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';

@Component({
    selector: 'core-login',
    templateUrl: './signin.component.html',
    styleUrls: ['./signin.component.css']
})

export class SigninComponent implements OnInit {

    loginForm: FormGroup;

    constructor(private fb: FormBuilder) {
        this.loginForm = fb.group({
            defaultFormUser: ['', Validators.required],
            defaultFormPass: ['', [Validators.required, Validators.minLength(8)]]
        });
    }

    ngOnInit() {

    }

    onSubmit() {

    }

}
