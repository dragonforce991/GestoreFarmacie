<template>
      <div class="container"> 
        <v-navigation-drawer absolute right expand-on-hover >
          <v-list subheader>
            <v-subheader>Chat Recenti</v-subheader>
      
            <v-list-item v-for="(u, index) in chats" :key="`user-${index}`" @click.prevent>
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
            <v-text-field
              v-model="message"
              label="Message..."
              outlined
              :rules="rules"
              append-icon="mdi-send-circle-outline"
              @keyup.enter="saveMessage"
            />
          </v-form>
          <!-- <ChatForm />  -->
        </div> 
      </div>
</template>

<script>
import firebase from '~/plugins/firebase.js'
import Message from "@/components/Message";
const db = firebase.firestore()
export default {
  layout: 'dashboard',
  components: {Message},
  data() {
    return {
      username: '',
      message: null,
      messages: [],
      chats : [],
      fullName : '',
      rules: [v => !!v || "Text is required"],
    }
  },
  created() {
    this.setUserName();
    this.fetchMessages();
    this.getChats();    
  },
  computed : {
    user(){
      return this.$store.state.user;
    }
  },
  methods: {
    async getChats(){
      const chatArray = [];
      try {
        const response = await this.$axios.$get('/user/getUsers');
        response.forEach(el => {
          chatArray.push({name : el.name + ' ' + el.surname, icon : 'mdi-account-circle'});
        })
      } catch (e) {
        console.log(e);
      } 

      this.chats = chatArray;
    },
    setUserName(){
      this.username= this.$store.state.user.email;
      this.fullName = this.$store.state.user.name + ' ' + this.$store.state.user.surname 
    },
    saveMessage() {
      const vm = this
      if (this.message.trim()) {
        db.collection('chat')
          .add({
            message: this.message,
            createAt: new Date(),
            username: this.username,
            fullName : this.fullName
            //user: [{username},{user:2}]
          })
          .then(function (docRef) {
            vm.message = null
          })
          .catch(function (error) {
            // eslint-disable-next-line no-console
            console.error('Error adding document: ', error)
          })
      }
    },
    fetchMessages() {
      const vm = this
      db.collection('chat')
        .orderBy('createAt')
        .onSnapshot((querySnapshot) => {
          const allMessages = []
          querySnapshot.forEach((doc) => {
            allMessages.push(doc.data())
          })
          vm.messages = allMessages
        })
    },
  },
}
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
</style>
