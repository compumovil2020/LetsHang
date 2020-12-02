import { Injectable } from '@angular/core';
import { AngularFireAuth } from '@angular/fire/auth';
import { AngularFireDatabase, AngularFireList, AngularFireObject} from '@angular/fire/database';
import { DataSnapshot } from '@angular/fire/database/interfaces';
import { EmailValidator } from '@angular/forms';
import { promise } from 'protractor';
import { Observable } from 'rxjs';
import { switchMap, map } from 'rxjs/operators';
import { AngularFireStorage, AngularFireStorageReference } from '@angular/fire/storage';



export interface User{
  uid?: string;
  email?: string;
  name?: string;
}



export interface Men{
  cuerpo:string;
  fecha: number;
  admin: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  currentUser: User = null;
  studentsRef: AngularFireList<any>; 
  usersObservable: Observable<User[]>;
  otherUser: User;
  storageRef: AngularFireStorageReference;



  constructor(private afAuth: AngularFireAuth, 
    private db: AngularFireDatabase,
    private storage: AngularFireStorage) {
    this.getAllUsers();
    this.afAuth.onAuthStateChanged(user => {
      console.log('user changed ', user);
      this.currentUser = user;
    });


    //this.storageRef = storage.ref(`images`);
   }

   public async signUp({email, password}){
    const credential = await this.afAuth.createUserWithEmailAndPassword(
      email,
      password
    );

    console.log('result: ', credential);
    const uid = credential.user.uid;
   }

   public signIn({email, password}){
     return this.afAuth.signInWithEmailAndPassword(email, password);
   }

   public signOut(): Promise<void>{
     return this.afAuth.signOut();
   }

   public addChatMessage(msg){

    console.log("estoy en el service, el mensaje es ", msg);
    const userChatRef = this.db.list('chats-admin/'+this.otherUser.uid);

    return userChatRef.push({
      admin: true,
      cuerpo: msg,
      fecha:  Math.floor(Date.now() / 1000),
      IDUsuario: "Z0Rr4C0Ch1N4"
    });
    

   }

   public getMessages (){
    return this.db.list('chats-admin/'+this.otherUser.uid).valueChanges() as Observable<Men[]>;
   }



   public getAllUsers(){      
    this.usersObservable =  this.db.list('users').snapshotChanges().pipe(map(changes =>
        changes.map((c )=>
          ({ uid: c.payload.key, name: c.payload.val()["name"] , email: c.payload.val()["email"] })
        )
      )
    ) as Observable<User[]>;

   }

   public getUsers(){
     return this.usersObservable;
   }

   public chatWith(user){
    this.otherUser = user;

   }

   public getOtherUserName():string{
     return this.otherUser.name;
   }

  public getImageURL(userID){
    
    this.storage.ref('images').child('profile').child("GOzT608JfNegVzQtbS4uk8pNKQR2").child('profilePic.jpg').getDownloadURL().subscribe( url => console.log(url) , err => {});
    return "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png";
    
  }
   
}


//https://firebasestorage.googleapis.com/v0/b/lets-hang-7c528.appspot.com/o/images%2Fprofile%2FTMLwdDGzXbavUKtlN5wO69yk7582%2FprofilePic.jpg