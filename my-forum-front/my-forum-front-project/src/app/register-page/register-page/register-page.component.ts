import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RegisterRequest } from './registerRequest';
import { Form } from '@angular/forms';
import { UserService } from '../../services/user.service';


@Component({
  selector: 'app-register-page',
  standalone: true,
  imports: [ ],
  templateUrl: './register-page.component.html',
  styleUrl: './register-page.component.css',
  providers: [Router, UserService]
})
export class RegisterPageComponent implements OnInit{

  private myForm : any;
  public isMyFormValid : boolean = false;
  public errorMessage: string = "";
  public registerRequest: RegisterRequest = {} as RegisterRequest;
  constructor(private router: Router,
            private userService: UserService){
              this.registerRequest.username = "";
              this.registerRequest.password = "";
              this.registerRequest.email = "";
            }

  ngOnInit(): void {
      this.myForm = document.forms[0];
  }

onInput(){
  this.isMyFormValid = this.myForm.checkValidity();
}

onInputUsername(event: any){
  this.onInput();
  this.registerRequest.username = event.target.value;
}

onInputPassword(event: any){
  this.onInput();
  this.registerRequest.password = event.target.value;
}

onInputEmail(event: any){
  this.onInput();
  this.registerRequest.email = event.target.value;
}



registerUser() {
  
  const formData = new FormData();

  console.log(this.myForm);

  if(!this.myForm.checkValidity())
    {
      this.myForm.reset();
      this.errorMessage = "Not all data was input correctly.";
      return;
    }
    else{
      this.errorMessage = "";
    }
 
    console.log(this.registerRequest);

    this.userService.registerUser(this.registerRequest)
    .subscribe(
    {
    next: response => {
      // Handle success response
    }, 
    error: error => {},
      // Handle error response
    complete: () => {}
    });
    
}

}
