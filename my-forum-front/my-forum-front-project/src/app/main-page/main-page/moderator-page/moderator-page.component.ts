import { Component, OnInit } from '@angular/core';
import Comment from '../../../../interfaces/comment'
import { NavigationBarComponent } from '../../../partials/nav/navigation-bar/navigation-bar.component';
import { CommentsService } from '../../../services/comments.service';
import { ForumPost } from '../../../../interfaces/forum-post';
import { ModeratorPageService } from '../../../services/moderator-page.service';

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

  constructor(private commentsService: CommentsService, private moderatorPageService: ModeratorPageService){
    this.allComments      = [];
    this.pendingComments  = [];
    this.allForumPosts    = [];
    this.pendingPosts     = [];
  };

  ngOnInit(): void {
      this.moderatorPageService.findAllForumPosts().subscribe({
        next: response => {this.allForumPosts = response;
                          this.pendingPosts = this.allForumPosts.filter(post => post.status === "PENDING");
                        },
        error: errorObj => console.error(errorObj)
      });

      this.moderatorPageService.findAllComments().subscribe({
        next: (response: any) => {this.allComments = response; 
                           this.pendingComments = this.allComments.filter(comment => comment.status === "PENDING");
                          },
        error: errorObj => console.error(errorObj)
      });
  }

  openCommentDialogue(selectedComment: Comment){
    console.log(selectedComment);
  }

  openForumPostDialogue(selectedForumPost: ForumPost){
    console.log(selectedForumPost);
  }

}
