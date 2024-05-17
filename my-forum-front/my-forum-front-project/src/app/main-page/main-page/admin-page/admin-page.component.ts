import { ChangeDetectorRef, Component, ElementRef, HostListener, OnDestroy, OnInit } from '@angular/core';
import { NavigationBarComponent } from '../../../partials/nav/navigation-bar/navigation-bar.component';
import { UserService } from '../../../services/user.service';
import { User } from '../../../../interfaces/user';
import { response } from 'express';
import { unsubscribe } from 'node:diagnostics_channel';
import { Observable } from 'rxjs';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { JsonPipe } from '@angular/common';
import { PermissionService } from '../../../services/permission.service';
import { Permission } from '../../../../interfaces/pemission';

@Component({
  selector: 'app-admin-page',
  standalone: true,
  imports: [NavigationBarComponent, FormsModule, ReactiveFormsModule, JsonPipe],
  templateUrl: './admin-page.component.html',
  styleUrl: './admin-page.component.css',
  providers:[UserService, PermissionService]
})
export class AdminPageComponent implements OnInit, OnDestroy{
  
  public allUsers: User[];
  public errorMessage: string;
  public selectedUser: User;
  public findAllSubscription: any;
  public userPermissions: Permission[];
  public selectedPermission: Permission | null;

  constructor(private userService: UserService, private permissionService: PermissionService,
    private cdr: ChangeDetectorRef){
    this.allUsers = [];
    this.errorMessage = "";
    this.selectedUser = this.initializeEmptyUser();
    this.userPermissions = [];
    this.selectedPermission = null;
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


  selectUser(userToSelect: string) {
      if(userToSelect === "null")
        {
          
          this.selectedUser = this.initializeEmptyUser();
          this.userPermissions = [];
          
         // this.cdr.detectChanges();
          this.updateForm();
          return;
        }
      else
      {
        this.selectedUser = JSON.parse(userToSelect);
        this.getPermissionsForUser(this.selectedUser.id);
        this.cdr.detectChanges();
        this.updateForm();
      }

    }

  public getPermissionsForUser(user_id: number){
    this.permissionService.findPermissionsByUserId(user_id).subscribe(
      {next: response => {
        this.userPermissions = response;
      },
      error: error => {
        this.userPermissions = [];
      },
      complete: () => {}
    })
  }
  public updateForm()
  {
    let myForm = document.forms[0];

    if (this.selectedUser.id == -1) {
      myForm["userId"].value = "";
    } else {
      myForm["userId"].value = this.selectedUser.id;
    }
    myForm["email"].value = this.selectedUser.email;
  }

  private initializeEmptyUser(): User {
    return { id: -1, username: "", email: "", createTime: "", status: "", type: "" };
  }

  selectPermission(permission: Permission, event: Event) {
    event.stopPropagation();
    if(this.selectedPermission === permission){
        this.selectedPermission = null;
        return;
        }
    else{
      this.selectedPermission = permission;
        }
    }

  isSelected(thisPermission: Permission) : boolean{
      if(thisPermission == this.selectedPermission)
        return true;
      else
        return false;
      }
    
  deselectPermission() {
    if(this.userPermissions.length > 0)
      {
        this.selectedPermission = null;
      }
    else{
      return;
      }
    }

    deletePermission(event: MouseEvent) {
      event.stopPropagation();
      this.permissionService.deletePermissionById(this.selectedPermission?.id as number).subscribe(
        response => {
        this.userPermissions = this.userPermissions.filter(p => p.id != this.selectedPermission?.id);
        this.selectedPermission = null;
        
        this.cdr.detectChanges();
      },
      error => {
        console.error("Failed to delete the permission", error);
      }
    );
  }

}
