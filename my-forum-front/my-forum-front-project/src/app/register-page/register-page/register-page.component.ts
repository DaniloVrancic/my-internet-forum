import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RegisterRequest } from './registerRequest';
import { Form } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { User } from '../../../interfaces/user';


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

  if(!this.myForm.checkValidity())
    {
      this.myForm.reset();
      this.errorMessage = "Not all data was input correctly.";
      return;
    }
    else{
      this.errorMessage = "";
    }

    this.userService.registerUser(this.registerRequest)
    .subscribe(
    {
    next: response => {
      let user: User = {} as User;
      user = response;
      this.userService.setCurrentUser(user);
      this.errorMessage = ""; // Remove the error message at this point cause everything went alright.

      if(user.status == "REQUESTED")
        {
          this.router.navigate(["/verify-page"]); //redirect the user to verification if he hasn't already been verified
        }
      else{
        this.router.navigate(["/main-page"]);
      }
    }, 
    error: error => {
      if(error.status === 409)
        {
          this.errorMessage = "Username already taken.";
        }
      else{
          this.errorMessage = "Something went wrong.";
      }
      console.error(this.errorMessage);
    },
    complete: () => {}
    });
    
}

}
