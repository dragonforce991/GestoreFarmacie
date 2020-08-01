<template>
  <loginForm v-if="logged===false" v-on:isLogged="isLogged"></loginForm>
  <homePage :user="user" v-else ></homePage>
</template>

<script>
import loginForm from "./components/loginForm"
import homePage from "./components/homePage"
 export default {
   name : 'App',
   components: {
     loginForm,
     homePage
   },
   methods:{
     isLogged(login, user){
       this.logged= login;
       this.user = user;
     }
   },
   data(){
     return {
       logged : false,
       user : undefined
     }
   }
 }
</script>

