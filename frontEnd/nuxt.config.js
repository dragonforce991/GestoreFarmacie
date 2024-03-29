import colors from 'vuetify/es5/util/colors'
import it from 'vuetify/es5/locale/it';
export default {
  // Global page headers (https://go.nuxtjs.dev/config-head)

  head: {
    titleTemplate: '%s - frontEnd',
    title: 'frontEnd',
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: '' }
    ],

    script: [
      { src: "/color-scheme.min.js", defer: true }
    ],

    link: [
      { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }
    ]
  },

  // Global CSS (https://go.nuxtjs.dev/config-css)
  css: [
  ],

  // Plugins to run before rendering page (https://go.nuxtjs.dev/config-plugins)
  plugins: [
    '~/plugins/notifier.js',
    '~/plugins/rules.js',
    '~/plugins/advancedVuetify.js',
    '~/plugins/firebase.js',
    '~/plugins/filter.js',
    '~/plugins/statistics.js',
    '~/plugins/filters.js',
	{ src: '~/plugins/coreUI-charts.js', mode: 'client' }
  ],

  // Auto import components (https://go.nuxtjs.dev/config-components)
  components: true,

  // Modules for dev and build (recommended) (https://go.nuxtjs.dev/config-modules)
  buildModules: [
    // https://go.nuxtjs.dev/vuetify
    '@nuxtjs/vuetify',
  ],
  modules: [
    '@nuxtjs/axios',
    [
      '@nuxtjs/firebase',
      {
        config: {
          apiKey: "AIzaSyAOGhGg-6AAeRp-2PciPsvyXDDaooamhhU",
          authDomain: "gestorefarmaciechat.firebaseapp.com",
          databaseURL: "https://gestorefarmaciechat.firebaseio.com",
          projectId: "gestorefarmaciechat",
          storageBucket: "gestorefarmaciechat.appspot.com",
          messagingSenderId: "781009942511",
          appId: "1:781009942511:web:86661eb9ed89f965e9681a"
        },
        services: {
          firestore: {
            memoryOnly: false
          }
        }
      }
    ]
  ],

  // Axios module configuration (https://go.nuxtjs.dev/config-axios)
  axios: {
    credentials: true,
		baseURL: 'http://localhost:8082/api'
  },

  // Vuetify module configuration (https://go.nuxtjs.dev/config-vuetify)
  vuetify:
  {
    customVariables: ['~/assets/variables.scss'],

    lang: {
      locales: { it },
      current: 'it',
    },

    theme:
    {
      dark: false,
      themes:
      {
        light: {
          primary: '#4caf50'
        },

        dark: {
          primary: '#1b5e20'
        }
      }
    }
  },

  // Build Configuration (https://go.nuxtjs.dev/config-build)
  build: {
    transpile: [/^advanced-vuetify($|\/)/]
  }
}
