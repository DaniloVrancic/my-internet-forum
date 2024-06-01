import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ForumPost } from '../../../../../interfaces/forum-post';
import { ForumerPageService } from '../../../../services/forumer-page.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-edit-post',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './edit-post.component.html',
  styleUrl: './edit-post.component.css',
  providers: [ForumerPageService]
})
export class EditPostComponent implements OnInit {

  inputTitle: string;
  inputContent: string;

  constructor(@Inject(MAT_DIALOG_DATA) public caughtPost: ForumPost, 
  private forumerPageService: ForumerPageService){
    this.inputTitle = caughtPost.title;
    this.inputContent = caughtPost.content;
  }

  ngOnInit(): void {
      
  }

  editPost(){
    this.forumerPageService.updateForumPost({id: this.caughtPost.id, title: this.inputTitle, content: this.inputContent, status: this.caughtPost.status})
    .subscribe({
      next: result => {
        console.log(result);
        alert("Successfully edited post.");
      }
    });
  }
}
