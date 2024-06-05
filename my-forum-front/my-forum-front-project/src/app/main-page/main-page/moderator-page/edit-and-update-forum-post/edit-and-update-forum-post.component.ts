import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ForumPost } from '../../../../../interfaces/forum-post';
import { CommentsService } from '../../../../services/comments.service';
import { ModeratorPageService } from '../../../../services/moderator-page.service';
import { environment } from '../../../../../environments/environment';
import { ForumerPageService } from '../../../../services/forumer-page.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-edit-and-update-forum-post',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './edit-and-update-forum-post.component.html',
  styleUrl: './edit-and-update-forum-post.component.css',
  providers:[ForumerPageService]
})
export class EditAndUpdateForumPostComponent implements OnInit{

  public possibleStatuses: string[] = environment.possibleStatuses;
  public isPostChanged: boolean = false;
  public selectedStatusName: string;

  public inputTitle: string;
  public inputContent: string;

  

  private contentTextArea = {} as any;

  constructor(@Inject(MAT_DIALOG_DATA) public caughtPost: ForumPost, 
                                       private moderatorPageService: ModeratorPageService,
                                       private forumerPageService: ForumerPageService){
          this.selectedStatusName = caughtPost.status;
          this.inputTitle = caughtPost.title;
          this.inputContent = caughtPost.content;
  }

  ngOnInit(): void {
    this.contentTextArea = document.getElementById("text-content") as any;
}

selectStatusName(statusName: string) {
  this.selectedStatusName = statusName;
  }

updatePostClick() {
  this.forumerPageService.updateForumPost({id: this.caughtPost.id, title: this.inputTitle, content: this.inputContent, status: this.selectedStatusName})
  .subscribe({next: response => {alert("Updated element ID: " + response.id)}
});
  }

  public updateIsPostChanged(){
    let starterStatus = this.caughtPost.status;
    
    if((this.caughtPost.status !== this.selectedStatusName) || (this.contentTextArea?.value !== this.caughtPost.content)){
      this.isPostChanged = true;
    }
    else{
      this.isPostChanged = false;
    }

  }

}
