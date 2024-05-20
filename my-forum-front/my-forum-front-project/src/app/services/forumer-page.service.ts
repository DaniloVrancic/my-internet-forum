import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ForumerPageService {

  selectedTopicId: number | null;

  constructor() { 
    this.selectedTopicId = null;
  }

  getSelectedTopicId(){
    if(this.getSelectedTopicId == null || this.selectedTopicId as number < 0){
      return null;
    }
    else{
      return this.selectedTopicId;
    }
  }

  setSelectedTopicId(value: number){
    this.selectedTopicId = value;
  }
}
