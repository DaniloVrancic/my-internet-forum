import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { NavigationBarComponent } from '../../../partials/nav/navigation-bar/navigation-bar.component';
import { UserService } from '../../../services/user.service';
import { User } from '../../../../interfaces/user';
import { response } from 'express';
import { unsubscribe } from 'node:diagnostics_channel';
import { Observable } from 'rxjs';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

@Component({
  selector: 'app-admin-page',
  standalone: true,
  imports: [NavigationBarComponent, FormsModule, ReactiveFormsModule],
  templateUrl: './admin-page.component.html',
  styleUrl: './admin-page.component.css',
  providers:[UserService]
})
export class AdminPageComponent implements OnInit, OnDestroy{

  
  public allUsers: User[];
  public errorMessage: string;
  public selectedUser: User;
  public findAllSubscription: any;

  constructor(private userService: UserService, private cdr: ChangeDetectorRef){
    this.allUsers = [];
    this.errorMessage = "";
    this.selectedUser = {id: -1, username: "", email: "", createTime: "", status: "", type: ""};
  }


  ngOnInit(): void {
    this.findAllSubscription = this.userService.findAll().subscribe(
      {
      next: response => {
        console.log(response);
        this.allUsers = response; 
      }, 
      error: error => {
        if(error.status === 403)
          {
            this.errorMessage = "Unauthorized.";
          }
        else if(error.status === 404){
          this.errorMessage = "Incorrect user data.";
        }
        else{
            this.errorMessage = "Something went wrong.";
        }
        console.error(this.errorMessage);
      },
      complete: () => {
        this.cdr.detectChanges();
      }
      });
  }

  ngOnDestroy(): void {
      this.findAllSubscription.unsubscribe();
  }


  selectUser(userToReturn: User) {
      console.log(userToReturn);
      this.selectedUser = userToReturn;
      this.cdr.detectChanges();
      this.updateForm();
    }

  selectNone() {
      console.log("selected NONE");
      this.selectedUser = {id: -1, username: "", email: "", createTime: "", status: "", type: ""};
      this.cdr.detectChanges();
      this.updateForm();
    }

  public updateForm()
  {
    let myForm = document.forms[0];

    console.log("FORM WILL UPDATE NOW.");
  }

}
