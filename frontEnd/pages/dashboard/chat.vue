<template>
  <div class = "wrapper"> 
        <v-navigation-drawer absolute right expand-on-hover >
          <v-list subheader>
            <div>
              <v-subheader>Chat Recenti</v-subheader>
              <v-divider></v-divider>
                <v-dialog
                  v-model="dialog"
                  width="500"
                > 
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
                <v-text-field
                  v-show = "isGroupNameVisible"
                  v-model="groupName"
                  label="Nome Gruppo..."
                  outlined
                  :rules="rules"
                />
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn
                    :disabled="createDisabled"
                    color="primary"
                    @click="createNewChat"
                  >
                    Crea
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-dialog> 
              
            </div>
            <v-list-item class = "left" v-for="(u, index) in chats" :key="`user-${index}`" @click="setChat(u)">
              <v-list-item-icon>
                <v-icon color='primary'>{{ u.icon }}</v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title v-text="u.name"></v-list-item-title>
              </v-list-item-content>
            </v-list-item>
          </v-list>
        </v-navigation-drawer>
        <div
        class="chat" ref="chat">
          <div class="mesgs" >
          <Message
            v-for="(message, index) in messages"
            :key="`message-${index}`"
            :message="message"
            :owner="message.username === username"
          /> 
        </div>
        </div>
        
        <div class="chat__form"> 
           <ChatForm v-show = "actualChat != ''" @sendMessage="saveMessage($event)"/> 
        </div>     
  </div>

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
      users : [],
      groupName : ''
    };
  },
    mounted()
  {
    this.$store.commit('appBar/setVisible', true);
    this.$store.commit('appBar/setButtons', {
      addButton: true,
      deleteButton: false,
      cloneButton: false,
      excelButton: false,
      printerButton: false,
    });

    this.focusSearchUnsubscriber = this.$store.subscribe((mutation) =>
    {
      switch (mutation.type)
      {
        case 'appBar/addClicked': this.add();	break;
      }
    });
  },
  created() {
    //this.prova();
    this.getUsers();
    this.setUserName();
    //this.fetchMessages();
    this.getChats();
  },
  watch: {
    messages() {
      setTimeout(() => {
        if (this.$refs.chat) {
          this.$refs.chat.scrollTop = this.$refs.chat.scrollHeight;
        }
      }, 0);
    },
  },
  computed: {
    user() {
      return this.$store.state.user;
    },
    isGroupNameVisible() {
      var count = 0;
      this.users.forEach(el => {
        if (el.selected)
          count ++;
      })
      return count>1;
    },
    createDisabled() {
      if(this.isGroupNameVisible){
        console.log(!!this.groupName);
        return !(!!this.groupName);
      }
      return false;
    }

  },
  methods: {
    add(){
      this.dialog = true;
    },
    async setChat(user) {
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
          });
      }
      this.actualChat = user.id;
    },
    async getUsers(){
      const response = await this.$axios.$get("/user/getUsersChat");
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
      var fullNameOtherUser
      const selected = []
      this.users.forEach((el) =>{
        if(el.selected){
          fullNameOtherUser = el.name + " " + el.surname
          selected.push(el.id)
        }
        el.selected = false;
      })
      selected.push(this.username)
      var isError = false;
      this.chats.forEach(el => {
        if(this.arrayEquals(el.users,selected)){
          isError = true;
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
          Name : this.$store.state.user.name + " " + this.$store.state.user.surname +"-"+fullNameOtherUser,
          users : selected,
          createAt : new Date(),
          isGroup : false,
          lastModified : new Date(),
          lastModifiedBy : this.username
        }
      }else{
        newChat = {
          Name : this.groupName,
          users : selected,
          createAt : new Date(),
          isGroup : true,
          lastModified : new Date(),
          lastModifiedBy : this.username
        }
      }
      console.log('newChat',newChat);
      console.log('Logged username',this.$store.state.user.name + " " + this.$store.state.user.surname);
      console.log('Other username' , fullNameOtherUser)
      const rs = await db.collection('chat')
        .add(newChat);
      this.dialog = false;  
      this.groupName = '';
    },

    async getChats() {
      try {
        const vm = this;
        db
          .collection("chat")
          .where("users", "array-contains", this.username)
          .orderBy("lastModified")
          .onSnapshot(query => {
            const chatArray = [];
            query.forEach(el =>{
              const v = el.data();
              var name = v.Name.split("-")[0] === this.fullName ? v.Name.split("-")[1] : v.Name.split("-")[0]
              chatArray.splice(0,0,{
                name: name,
                icon: v.isGroup ? "mdi-account-group" : "mdi-account-circle",
                isUser: v.isGroup ? false : true,
                id: el.id,
                users : v.users
              });
             
            })
             vm.chats = chatArray;
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
            db.collection("chat")
            .doc(vm.actualChat)
            .update({
              "lastModified" : new Date(),
              "lastModifiedBy" : vm.username
            })
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
.wrapper {
  height: 100%;
  position: relative;
  overflow: hidden;
}
.chat__form {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 1rem;
  height: 80px;
  margin-right : 15%;
}
.mesgs {
  margin-right: 15%;
}
.right {
  margin-top: 10px;
  margin-right: 10px;
  float: right;
}
.left {
  float: left;
}
.chat {
  position: absolute;
  top: 0;
  right: 0;
  left: 0;
  bottom: 80px;
  padding: 1rem;
  overflow-y: auto;
  color: #000;
}
</style>
