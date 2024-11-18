// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  app:{
    head:{
      title: 'Linknote',
      link: [
        {
          rel: 'icon',
          type: 'image/x-icon',
          href: '/img/LinkNote.png',
        },
      ]
    }
  },
  compatibilityDate: "2024-04-03",
  devtools: { enabled: true },
  components: true,
  css: ["~/assets/css/main.css"],
  postcss: {
    plugins: {
      tailwindcss: {},
      autoprefixer: {},
    },
  },
  runtimeConfig: {
    public: {
      authApiUrl: "http://127.0.0.1:8080",
    },
  },
});
