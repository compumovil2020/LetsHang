import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { IonContent } from '@ionic/angular';
import { Observable } from 'rxjs';
import { ChatService, Men } from 'src/app/services/chat.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.page.html',
  styleUrls: ['./chat.page.scss'],
})
export class ChatPage implements OnInit {
  @ViewChild(IonContent) content: IonContent;

  mensajes: Observable<Men[]>;
  userName: string;
  newMessage: string = '';

  constructor(private chatService: ChatService, private router: Router) {
    this.mensajes = chatService.getMessages();
    this.userName = chatService.getOtherUserName();
   }

  ngOnInit() {
  }

  sendMessage(){
    console.log("estoy haciendo click, mi mensaje es ", this.newMessage );
    this.chatService.addChatMessage(this.newMessage).then(() => {
      this.newMessage = '';
      this.content.scrollToBottom;
    });
  }

  signOut(){
    this.chatService.signOut().then(() => {
      this.router.navigateByUrl('/', {replaceUrl: true});
    });
  }

  formatDate(unixTime){
    return new Date(unixTime).toLocaleString()

  }


}
