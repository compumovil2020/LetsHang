import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { ChatService, User } from 'src/app/services/chat.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.page.html',
  styleUrls: ['./user-list.page.scss'],
})
export class UserListPage implements OnInit {

  users: Observable<User[]>;
  tam: number = 0;
  blankProfilePath = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png";

  constructor(private chatService: ChatService, private router: Router) {
    this.users = chatService.getUsers();

    this.users.subscribe(u => {
      this.tam = u.length;
      console.log(u[0].name);
      console.log(JSON.stringify(u));
    });
   }

  ngOnInit() {
  }

  chatWith(user: User){
    console.log("voy a chatear con", user.name);
    this.chatService.chatWith(user);
    this.router.navigateByUrl('/chat');
  }

  getImageURL(userID:string){
    let inicio = "https://firebasestorage.googleapis.com/v0/b/lets-hang-7c528.appspot.com/o/images%2Fprofile%2F";
    let fin = "%2FprofilePic.jpg?alt=media&token=31779264-9361-4d7a-b26c-9ee9a0a19b65";
    return inicio+userID+fin;
  }

  onImgError(event) { 
    event.target.src = 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png';
  }

}
