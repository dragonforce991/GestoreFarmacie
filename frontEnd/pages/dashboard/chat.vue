<template>
<v-container> 
        <v-navigation-drawer absolute right expand-on-hover >
          <v-list subheader>
            <div>
              <div class = "left">
                <v-subheader>Chat Recenti</v-subheader>
              
              </div>
              <div class = "right">
                    <v-dialog
                      v-model="dialog"
                      width="500"
                    > 
                    <template v-slot:activator="{ on, attrs }">
                    <v-btn
                      color="primary"
                      v-bind="attrs"
                      v-on="on"
                      small
                    >
                      +
                    </v-btn>
                  </template>

                  <v-card>
                    <v-card-title class="headline grey lighten-2">
                      Nuovo
                    </v-card-title>
                    <v-list-item v-for="(u, index) in users" :key="`user-${index}`" >
                      <v-checkbox
                      v-model="u.selected"
                      :label="u.name + u.surname"
                      ></v-checkbox>
                    </v-list-item>
                    
                    

                    <v-divider></v-divider>

                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <v-btn
                        color="primary"
                        
                        @click="createNewChat"
                      >
                        Crea
                      </v-btn>
                    </v-card-actions>
                  </v-card>
                </v-dialog>
                
              </div>
              
            </div>
            
            <v-list-item class = "left" v-for="(u, index) in chats" :key="`user-${index}`" @click="setChat(u)">
              <v-list-item-icon>
                <v-icon color='primary'>mdi-account-circle-outline</v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title v-text="u.name"></v-list-item-title>
              </v-list-item-content>
            </v-list-item>
          </v-list>
        </v-navigation-drawer>
        <div class="mesgs">
          <Message
            v-for="(message, index) in messages"
            :key="`message-${index}`"
            :message="message"
            :owner="message.username === username"
          /> 
        </div>
        <div class="chat__form"> 
          <v-form
          >
          </v-form>
           <ChatForm @sendMessage="saveMessage($event)"/> 
        </div>     
</v-container>

</template>

<script>
import firebase from "~/plugins/firebase.js";
import Message from "@/components/Message";
import ChatForm from "@/components/ChatForm";
const db = firebase.firestore();
export default {
  layout: "dashboard",
  components: { Message, ChatForm },
  data() {
    return {
      username: "",
      messages: [],
      chats: [],
      fullName: "",
      rules: [(v) => !!v || "Text is required"],
      actualChat: "",
      dialog : false,
      users : []
    };
  },
  created() {
    //this.prova();
    this.getUsers();
    this.setUserName();
    //this.fetchMessages();
    this.getChats();
  },
  computed: {
    user() {
      return this.$store.state.user;
    },
  },
  methods: {
    async setChat(user) {
      console.log("user", user);
      const vm = this;
      if (user.isUser) {
        const chat = await db
          .collection("chat")
          .doc(user.id)
          .collection("messaggi")
          .orderBy("createAt")
          .onSnapshot((querySnapshot) => {
            const allMessages = [];
            querySnapshot.forEach((message) => {
              message.data();
              allMessages.push(message.data());
            });
            vm.messages = allMessages;
          })
          
      }
      if (!user.isUser) {
        console.log("non user");
        const chat = await db.collection("chat").doc(user.id).get();
        db.collection("chat")
          .doc(user.id)
          .collection("messaggi")
          .onSnapshot((querySnapshot) => {
            const allMessages = [];
            querySnapshot.forEach((message) => {
              message.data();
              allMessages.push(message.data());
            });
            vm.messages = allMessages;
            console.log("messages", vm.messages);
          });
      }
      this.actualChat = user.id;
    },
    async getUsers(){
      const response = await this.$axios.$get("/user/getUsers");
      response.forEach(el => {
        this.users.push({...el, selected : false});
      })
    },
    arrayEquals(a, b) {
      return Array.isArray(a) &&
        Array.isArray(b) &&
        a.length === b.length &&
        a.every((val, index) => val === b[index]);
    },
    async createNewChat() {
      var fullName
      const selected = []
      this.users.forEach((el) =>{
        if(el.selected)
          selected.push(el.id)
        el.selected = false;
        fullName = el.name + " " + el.surname
      })
      selected.push(this.username)
      console.log(this.chats);
      var isError = false;
      this.chats.forEach(el => {
        if(this.arrayEquals(el.users,selected)){
          isError = true;
        }else{
          console.log(el.users, selected)
        }
      })
      if(isError){
        this.dialog = false;
        this.$notifier.showError("Chat già esistente");
        return
      }
      if(selected.length == 1){
        this.dialog = false;
        this.$notifier.showError("Non puoi creare una chat da solo");
        return
      }
      let newChat;
      if(selected.length == 2){
        newChat = {
          Name : this.$store.state.user.name + " " + this.$store.state.user.surname +"-"+fullName,
          users : selected,
          createAt : new Date(),
          isGroup : false,
        }
      }else{
        newChat = {
          Name : "provaGruppo",
          users : selected,
          createAt : new Date(),
          isGroup : true,
        }
      }
      const rs = await db.collection('chat')
        .add(newChat);
      this.dialog = false;  
    },

    async getChats() {
      try {
        console.log(this.username)
        const vm = this;
        db
          .collection("chat")
          .where("users", "array-contains", this.username)
          .onSnapshot(query => {
            const chatArray = [];
            query.forEach(el =>{
              const v = el.data();
              
              var name = v.Name.split("-")[0] === this.fullName ? v.Name.split("-")[1] : v.Name.split("-")[0]
              chatArray.push({
                name: name,
                icon: v.isGroup ? "" : "mdi-account-circle",
                isUser: v.isGroup ? false : true,
                id: el.id,
                users : v.users
              });
             vm.chats = chatArray; 
            })
          });
        
      } catch (e) {
        console.log(e);
      }
    },
    setUserName() {
      this.username = this.$store.state.user.id;
      this.fullName =
        this.$store.state.user.name + " " + this.$store.state.user.surname;
    },
    saveMessage(message) {
      const vm = this;
        if(message != null && message != ""){
          const toInsert = {
            message: message,
            createAt: new Date(),
            username: this.username,
            fullName: this.fullName,
          };
          db.collection("chat")
          .doc(this.actualChat)
          .collection("messaggi")
          .add(toInsert)
          .then(function (docRef) {
            vm.message = null;
          })
          .catch(function (error) {
            // eslint-disable-next-line no-console
            console.error("Error adding document: ", error);
          });
       }
    },
  },
};
</script>
<style scoped>
.chat__form {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 1rem;
  height: 80px;
  margin-right: 15%;
}
.mesgs {
  margin-right: 12%;
}
.right {
  margin-top: 10px;
  margin-right: 10px;
  float: right;
}
.left {
  float: left;
}
</style>
