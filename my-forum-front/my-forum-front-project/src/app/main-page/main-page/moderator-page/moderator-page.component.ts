import { Component, OnInit } from '@angular/core';
import Comment from '../../../../interfaces/comment'
import { NavigationBarComponent } from '../../../partials/nav/navigation-bar/navigation-bar.component';
import { CommentsService } from '../../../services/comments.service';
import { ForumPost } from '../../../../interfaces/forum-post';
import { ModeratorPageService } from '../../../services/moderator-page.service';
import { MatDialog } from '@angular/material/dialog';
import { EditAndUpdateCommentComponent } from './edit-and-update-comment/edit-and-update-comment.component';
import { EditAndUpdateForumPostComponent } from './edit-and-update-forum-post/edit-and-update-forum-post.component';

@Component({
  selector: 'app-moderator-page',
  standalone: true,
  imports: [NavigationBarComponent],
  templateUrl: './moderator-page.component.html',
  styleUrl: './moderator-page.component.css',
  providers: [ModeratorPageService ,CommentsService]
})
export class ModeratorPageComponent implements OnInit{
  allComments: Comment[];
  pendingComments: Comment[];
  allForumPosts: ForumPost[];
  pendingPosts: ForumPost[];
  isPendingDisplaySelected : boolean = true;

  constructor(private commentsService: CommentsService, private moderatorPageService: ModeratorPageService,
        private dialog: MatDialog
  ){
    this.allComments      = [];
    this.pendingComments  = [];
    this.allForumPosts    = [];
    this.pendingPosts     = [];
  };

  ngOnInit(): void {
      this.moderatorPageService.findAllForumPosts().subscribe({
        next: response => {this.allForumPosts = response;
                           this.allForumPosts.forEach(forumPost => {
                            forumPost.title = this.decodeSanitizedString(forumPost.title);
                            forumPost.content = this.decodeSanitizedString(forumPost.content);
                            forumPost.user_creator = this.decodeSanitizedString(forumPost.user_creator);
                           });
                          this.pendingPosts = this.allForumPosts.filter(post => post.status === "PENDING");
                        },
        error: errorObj => console.error(errorObj)
      });

      this.moderatorPageService.findAllComments().subscribe({
        next: (response: any) => {this.allComments = response;
                          this.allComments.forEach(comment => {
                            comment.content = this.decodeSanitizedString(comment.content);
                            comment.username = this.decodeSanitizedString(comment.username);
                          });
                           this.pendingComments = this.allComments.filter(comment => comment.status === "PENDING");
                          },
        error: errorObj => console.error(errorObj)
      });
  }

  openCommentDialogue(selectedComment: Comment){
    const dialogRef = this.dialog.open(EditAndUpdateCommentComponent, {
      width: '70%',
      height: '70vh',
      maxHeight: '90vh',
      data: selectedComment
    });

    dialogRef.afterClosed().subscribe(result => {console.log(result)}).unsubscribe()
  }

  openForumPostDialogue(selectedForumPost: ForumPost){
    const dialogRef = this.dialog.open(EditAndUpdateForumPostComponent, {
      width: '70%',
      height: '70vh',
      maxHeight: '90vh',
      data: selectedForumPost
    });

    dialogRef.afterClosed().subscribe(result => {console.log(result)}).unsubscribe()
  }

  toggleDisplaySelected(){
    this.isPendingDisplaySelected = !this.isPendingDisplaySelected;
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
