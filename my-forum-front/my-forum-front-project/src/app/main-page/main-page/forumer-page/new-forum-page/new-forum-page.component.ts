import { Component, Inject } from '@angular/core';
import { ForumPost } from '../../../../../interfaces/forum-post';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';

@Component({
  selector: 'app-new-forum-page',
  standalone: true,
  imports: [],
  templateUrl: './new-forum-page.component.html',
  styleUrl: './new-forum-page.component.css'
})
export class NewForumPageComponent {

  constructor(@Inject(MAT_DIALOG_DATA) public allPosts: ForumPost[],
  public dialogRef: MatDialogRef<NewForumPageComponent>,
  private router: Router){

  }

  routeToPurchasePage() {
    this.router.navigate(["/forumer-page"]).then(() => {
      this.dialogRef.close();
    });;
    }

}
