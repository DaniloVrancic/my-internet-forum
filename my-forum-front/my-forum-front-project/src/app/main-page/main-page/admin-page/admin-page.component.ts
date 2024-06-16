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
import { Topic } from '../../../../interfaces/topic';
import { TopicService } from '../../../services/topic.service';
import { error } from 'node:console';
import { environment } from '../../../../environments/environment';
import { PermissionRequest } from '../../../../interfaces/requests/permission-request';

@Component({
  selector: 'app-admin-page',
  standalone: true,
  imports: [NavigationBarComponent, FormsModule, ReactiveFormsModule, JsonPipe],
  templateUrl: './admin-page.component.html',
  styleUrl: './admin-page.component.css',
  providers:[UserService, PermissionService, TopicService]
})
export class AdminPageComponent implements OnInit, OnDestroy{

  
  public findAllSubscription: any;
  public findTopicsSubscription: any; 


  public errorMessage: string;
  
  public userPermissions: Permission[]; //List of permissions of current user
  public allTopics: Topic[];
  public allUsers: User[];

  public possiblePermissions = environment.possiblePermissions;
  
  public selectedUser: User;
  public selectedPermission: Permission | null;
  public selectedTopic: Topic | null;
  private permissionRequest : PermissionRequest;

  constructor(private userService: UserService, private permissionService: PermissionService, private topicService: TopicService,
    private cdr: ChangeDetectorRef){
    this.allUsers = [];
    this.errorMessage = "";

    this.allTopics = [];
    this.userPermissions = [];

    this.selectedUser = this.initializeEmptyUser();
    this.selectedPermission = null;
    this.selectedTopic = null;
    this.permissionRequest = {user_id: -1, topic_id: -1, type: ""};
  }


  ngOnInit(): void {
    this.findAllSubscription = this.userService.findAll().subscribe(
      {
      next: response => {
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

      this.findTopicsSubscription = this.topicService.findAllTopics().subscribe({
        next: response => {
          this.allTopics = response;
        },
        error: error => {
          this.errorMessage = "Couldn't find any topics.";
        },
        complete: () => {this.cdr.detectChanges();}
      });
  }

  ngOnDestroy(): void {
      this.findAllSubscription.unsubscribe();
      this.findTopicsSubscription.unsubscribe();
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
        this.userPermissions = this.sortPermissionsByTopicName(response);
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

  selectPermissionName(permissionName: string) {
    this.permissionRequest.type = permissionName;
  }

  selectTopic(selectedTopicString: string){
    let selectedTopic: Topic = JSON.parse(selectedTopicString);
    if(selectedTopic != null)
      {
        this.selectedTopic = selectedTopic as Topic;
      }
  }

  canAddPermission(): boolean{
    if(this.selectedTopic == undefined && this.selectedTopic == null)
      {
        return false;
      }
    if(this.selectedTopic?.id > 0 && this.permissionRequest.type != ""){
      return true;
    }
    else{
      return false;
    }
  }

  addPermission() {
    this.permissionRequest.user_id = this.selectedUser.id;
    this.permissionRequest.topic_id = this.selectedTopic?.id as number;

    if(this.permissionRequest.type == null || this.permissionRequest.type == ""){
        return;
        console.error("Can't add non-existent type");
      }
    
      let userPermissionToAdd: Permission = {id: -1, topic_id: this.selectedTopic?.id as number, topic_name: this.selectedTopic?.name as string,
      type: this.permissionRequest.type, user_id: this.selectedUser.id
      };



      this.permissionService.addPermission(this.permissionRequest).subscribe({
        next: response => {
          this.userPermissions.push(response);
          this.sortPermissionsByTopicName(this.userPermissions);
        },
        error: error => {console.log(error)},
        complete: () => {}
        })
    }

    clearForm(){
      document.forms[0].reset();
      this.selectedPermission = null;
      this.selectedTopic = null;
      this.selectedUser = this.initializeEmptyUser();
      this.permissionRequest = {user_id: -1, topic_id: -1, type: ""};
    }

    private sortPermissionsByTopicName(myArray: Permission[]) : Permission[] {
      myArray.sort((a, b) => {
        const topicNameA = a.topic_name.toUpperCase();
        const topicNameB = b.topic_name.toUpperCase();
    
        if (topicNameA < topicNameB) {
          return -1;
        }
        if (topicNameA > topicNameB) {
          return 1;
        }
        return 0;
      });
      return myArray;
    }

    updateUser(selectedUser: User) {  //Maybe needs a fix
        let myForm = document.forms[0];

        
        let selectedStatus = myForm["status"].value;
        if(selectedStatus == null || selectedStatus.length == 0)
          {
            return;
          }
        
        let selectedType = myForm["type"].value;
        if(selectedType == null || selectedType.length == 0)
          {
            return;
          }

        if(this.selectedUser.status !== selectedStatus || this.selectedUser.type !== selectedType)
          {
            this.userService.changePrivilege({id: this.selectedUser.id, status: selectedStatus, type: selectedType}).subscribe({
              next: response => {this.selectedUser = response;
                let userToUpdate = this.allUsers.find(user => user.id === this.selectedUser.id);
                  if (userToUpdate) {
                    userToUpdate.status = this.selectedUser.status;
                    userToUpdate.type = this.selectedUser.type;
                    }
                  alert(`Updated user: ${response.username}\nStatus: ${response.status}\nType: ${response.type}`);
              },
              error: error => {console.error(error);}
            })
          }
      }

    isUpdateUserButtonDisabled(){ //Determines if the update button will stay disabled or not
      let myForm = document.forms[0];
      if(this.selectedUser.type === 'Administrator' || 
          myForm["status"].value == null || myForm["status"].value.length == 0 || 
          myForm["type"].value == null || myForm["type"].value.length == 0)
          {
            return true;
          }
      else
          {
            return false;
          }
    }

    decodeSanitizedString(value: string): string {
      if (value == null) {
        return "null";
      }
      return value.replace(/&amp;/g, "&")
                  .replace(/&lt;/g, "<")
                  .replace(/&gt;/g, ">")
                  .replace(/&quot;/g, "\"")
                  .replace(/&#39;/g, "'");
    }
}
