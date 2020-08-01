import Firebase from 'firebase/app'
import 'firebase/database'

const app = Firebase.initializeApp({
  apiKey: "AIzaSyBkuhofsaI7Ld2p0GOocCWP9JRck_GQwLA",
  authDomain: "games-manager-d7f3d.firebaseapp.com",
  databaseURL: "https://games-manager-d7f3d.firebaseio.com",
  projectId: "games-manager-d7f3d",
  storageBucket: "games-manager-d7f3d.appspot.com",
  messagingSenderId: "331545380104",
  appId: "1:331545380104:web:c83ef075a14b4457b318bd",
  measurementId: "G-8GWE68RLKW"
})  // configurazione per l’accesso al cloud
export const db = app.database() // recuperiamo il database
export const gamesRef = db.ref('Games') // recup rif alla collect.
