import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';

@Component({
    selector: 'core-login',
    templateUrl: './signup.component.html',
    styleUrls: ['./signup.component.css']
})

export class SignupComponent implements OnInit {

    loginForm: FormGroup;

    constructor(private fb: FormBuilder) {
        this.loginForm = fb.group({
            defaultFormEmail: ['', Validators.required],
            defaultFormPass: ['', [Validators.required, Validators.minLength(8)]]
        });
    }

    ngOnInit() {

    }
}
