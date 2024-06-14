import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { NavigationBarComponent } from '../../../partials/nav/navigation-bar/navigation-bar.component';
import { ForumerPageService } from '../../../services/forumer-page.service';
import { TopicService } from '../../../services/topic.service';
import { Topic } from '../../../../interfaces/topic';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ForumPost } from '../../../../interfaces/forum-post';

import { NewForumPageComponent } from './new-forum-page/new-forum-page.component';
import { UserService } from '../../../services/user.service';
import { MatIconModule} from '@angular/material/icon';
import { SelectedPostComponent } from './selected-post/selected-post.component';
import { EditPostComponent } from './edit-post/edit-post.component';
import { PermissionService } from '../../../services/permission.service';
import { environment } from '../../../../environments/environment';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-forumer-page',
  standalone: true,
  imports: [NavigationBarComponent, MatIconModule],
  templateUrl: './forumer-page.component.html',
  styleUrl: './forumer-page.component.css',
  providers: [ForumerPageService, TopicService, UserService, PermissionService]
})
export class ForumerPageComponent implements OnInit, OnDestroy{


  allTopics: Topic[];
  allPosts: ForumPost[];

  unauthorizedErrorMessage: string = "";

  forumerTopicPermissions: string[] = [];

  public forumerHasUpdate : boolean = false;
  public forumerHasDelete : boolean = false;
  public forumerHasAdd : boolean = false;

  constructor(private forumerPageService: ForumerPageService, private topicService: TopicService,
              private userService: UserService, private permissionService: PermissionService,
              private router: Router, private dialog: MatDialog, private cdr: ChangeDetectorRef
  ){
    this.forumerPageService.setSelectedTopicId(null);
    this.allTopics = [];
    this.allPosts  = [];
  }

  ngOnInit(): void {
      this.topicService.findAllTopics().subscribe({
        next: response => {this.allTopics = response;},
        error: (error:any) => {console.error(error);}
        }
      );
  }

  setTopicIdAndUpdatePosts(numberForSet: number) {
    this.forumerPageService.setSelectedTopicId(numberForSet);

    if(this.userIsForumer()){ //will only execute if the current user is a forumer type (no need for admins and moderators as they have all priviledges already)

    
    this.permissionService.findPermissionsForUserAndTopic({userId: this.userService.getCurrentUser()?.id as number, topicId: numberForSet}).subscribe({
      next: (response: string[]) => {this.forumerTopicPermissions = response;
                                    sessionStorage.setItem(environment.currentForumerTopicPermissions, JSON.stringify(response));
                                    console.log(this.forumerTopicPermissions);
                                    if(this.forumerTopicPermissions.includes("CREATE")){
                                      this.forumerHasAdd = true;
                                    }
                                    else{
                                      this.forumerHasAdd = false;
                                    }

                                    if(this.forumerTopicPermissions.includes("UPDATE")){
                                      this.forumerHasUpdate = true;
                                    }
                                    else{
                                      this.forumerHasUpdate = false;
                                    }

                                    if(this.forumerTopicPermissions.includes("DELETE")){
                                      this.forumerHasDelete = true;
                                    }
                                    else{
                                      this.forumerHasDelete = false;
                                    }

                                    this.cdr.detectChanges();

      },
      error: errorObj => {console.error(errorObj)}
    });
  }

    this.forumerPageService.findTop20ApprovedByTopicId(numberForSet).subscribe({
      next: response => {this.allPosts = response
        this.allPosts.forEach(forumPost => {
          forumPost.title = this.decodeSanitizedString(forumPost.title)
          forumPost.content = this.decodeSanitizedString(forumPost.content);
          forumPost.user_creator = this.decodeSanitizedString(forumPost.user_creator);
        })
      },
      error: errorObj => console.error(errorObj)
    })
    }

  addNewForumPost() {
      if(this.userHasAdministrationPriviledges() || (this.userIsForumer() && this.forumerHasAdd))
        {
          this.openDialog();
        }
      else{
        this.unauthorizedErrorMessage = "User doesn't have the proper permission to add messages";
        throw new HttpErrorResponse({status:403, statusText: this.unauthorizedErrorMessage});
      }
      }

  openDialog(){

    const dialogRef = this.dialog.open(NewForumPageComponent, {
      width: '70%',
      maxHeight: '90vh',
      data: this.allPosts
    });

    dialogRef.afterClosed().subscribe(result => {}).unsubscribe()
  }

  openPost(caughtPost: ForumPost){

    let clonedPost: any = {...caughtPost};
    if(this.userIsForumer())
      {
        clonedPost.forumerHasAdd = this.forumerHasAdd;
        clonedPost.forumerHasUpdate = this.forumerHasUpdate;
        clonedPost.forumerHasDelete = this.forumerHasDelete;
        clonedPost.topicId = this.forumerPageService.getSelectedTopicId();
      }
    const dialogRef = this.dialog.open(SelectedPostComponent, {
      width: '85%',
      autoFocus: false,
      maxHeight: '90vh',
      data: clonedPost
    });

  }



  topicIsSelected(): boolean{
    let currentTopic = this.forumerPageService.getSelectedTopicId();

    return currentTopic != null && currentTopic != undefined;
  }

  editPost(postToEdit: ForumPost, event: Event){

    if(this.userHasAdministrationPriviledges() || (this.userIsForumer() && this.forumerHasUpdate && this.userOwnerOfPost(postToEdit.user_creator))){

      event.stopPropagation();
      const dialogRef = this.dialog.open(EditPostComponent, {
        width: '85%',
        autoFocus: false,
        maxHeight: '90vh',
        data: postToEdit
        });

        dialogRef.afterClosed().subscribe((result: ForumPost) => {
          
          this.forumerPageService.findByPostId(postToEdit.id).subscribe({ //this is to update the state of the GUI (Makes the site more responsive)
            next: result => {
            if (result) {
              const index = this.allPosts.findIndex(post => post.id === result.id);
            if (index !== -1) {
              this.allPosts[index] = result;
              this.allPosts[index].title = this.decodeSanitizedString(result.title);
              this.allPosts[index].content = this.decodeSanitizedString(result.content);
              this.allPosts[index].user_creator = this.decodeSanitizedString(result.user_creator);
            }
          }
        },
            error: errorObj => {console.error(errorObj)}
          });



        });
    }
    else{
      this.unauthorizedErrorMessage = "User doesn't have the proper permission to update messages";
        throw new HttpErrorResponse({status:403, statusText: this.unauthorizedErrorMessage});
    }
  }

  deletePost(postToDelete: ForumPost, event: Event){

    if(this.userHasAdministrationPriviledges() || (this.userIsForumer() && this.forumerHasDelete && this.userOwnerOfPost(postToDelete.user_creator)))
      {
        event.stopPropagation();
        
        let acceptedDelete = confirm(`Are you sure you want to delete:\n${postToDelete.title} ?`);

        if(acceptedDelete){
          this.forumerPageService.deleteForumPost(postToDelete.id).subscribe({
            next: result => {
              if(result){
                this.allPosts = this.allPosts.filter(post => post.id !== postToDelete.id);
                alert(`Successfully deleted:\n${result.title}`);
              }
            },
            error: errorObj => {console.error(errorObj);}
          });
        }
      }
    else{
      this.unauthorizedErrorMessage = "User doesn't have the proper permission to delete messages";
        throw new HttpErrorResponse({status:403, statusText: this.unauthorizedErrorMessage});
    }
  }

  ngOnDestroy(): void {
      this.userService.deleteFetchedPermissions();
      this.forumerTopicPermissions = [];
      this.unauthorizedErrorMessage = "";
  }

  userIsLoggedIn() : boolean{
    let currentUser = this.userService.getCurrentUser()?.username;
    return currentUser != null && currentUser != undefined;
  }

  userHasAdministrationPriviledges() : boolean{
    let currentUserType = this.userService.getCurrentUser()?.type;
    return (currentUserType === "Administrator") || (currentUserType === "Moderator");
  }

  userIsForumer() : boolean{
    let currentUserType = this.userService.getCurrentUser()?.type;
    return (currentUserType === "Forumer");
  }

  userOwnerOfPost(postCreator: string) : boolean{
    let currentUser = this.userService.getCurrentUser();

    return currentUser?.username === postCreator;
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
