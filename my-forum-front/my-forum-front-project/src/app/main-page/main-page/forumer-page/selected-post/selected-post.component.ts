import { ChangeDetectorRef, Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { NavigationBarComponent } from '../../../../partials/nav/navigation-bar/navigation-bar.component';
import { ForumPost } from '../../../../../interfaces/forum-post';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import Comment from '../../../../../interfaces/comment';
import {MatFormFieldModule} from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { CommentsService } from '../../../../services/comments.service';
import { CommentRequest } from '../../../../../interfaces/requests/comment-request';
import { ForumerPageService } from '../../../../services/forumer-page.service';
import { UserService } from '../../../../services/user.service';
import { ForumerPageComponent } from '../forumer-page.component';
import { environment } from '../../../../../environments/environment';
import { PermissionService } from '../../../../services/permission.service';
import e from 'express';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-selected-post',
  standalone: true,
  imports: [NavigationBarComponent, MatFormFieldModule, FormsModule],
  templateUrl: './selected-post.component.html',
  styleUrl: './selected-post.component.css',
  providers:[CommentsService, ForumerPageService, UserService]
})
export class SelectedPostComponent implements OnInit, OnDestroy{

  commentsOnProgram: Comment[];
  commentToPost: string;

  forumerHasAdd: boolean = true;
  forumerHasUpdate: boolean = true;
  forumerHasDelete: boolean = true;
  unauthorizedErrorMessage: string = "";

  constructor(@Inject(MAT_DIALOG_DATA) public caughtPost: any, 
  private commentService: CommentsService,
  private forumerPageService: ForumerPageService,
  private userService: UserService,
  private permissionService: PermissionService,
  private cdr: ChangeDetectorRef){
    this.commentsOnProgram = [];
    this.commentToPost = "";
  }

  ngOnInit(): void {
      this.commentService.findAllApprovedByPostId(this.caughtPost.id)
      .subscribe({
        next: response => {this.commentsOnProgram = response},
        error: errorObj => {console.error(errorObj);}
      });

      if(this.userIsForumer()){
        this.forumerHasAdd = this.caughtPost.forumerHasAdd;
        this.forumerHasUpdate = this.caughtPost.forumerHasUpdate;
        this.forumerHasDelete = this.caughtPost.forumerHasDelete;
      }
  }

  postComment($event: MouseEvent) {
    if(this.userHasAdministrationPriviledges() || (this.userIsForumer() && this.forumerHasAdd))
      {
            let commentRequest: CommentRequest = {content: this.commentToPost, forum_post: this.caughtPost.id, user: this.userService.getCurrentUser()?.id as number}
            this.commentService.addComment(commentRequest).subscribe({
              next: response => { 
                if(this.userService.getCurrentUser()?.type == "Administrator" || this.userService.getCurrentUser()?.type == "Moderator")
                this.commentsOnProgram?.push(response); 
                alert("Comment successfully added.")},
              error: errorResponse => {alert("An error occured!");}  
            });
      }
      else{
        this.unauthorizedErrorMessage = "User doesn't have the proper permission to post comments.";
        throw new HttpErrorResponse({status:403, statusText: this.unauthorizedErrorMessage});
      }
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

    ngOnDestroy(): void {
        this.unauthorizedErrorMessage = "";
    }
}
