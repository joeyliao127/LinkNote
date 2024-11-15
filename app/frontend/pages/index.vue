<template lang="pug">
    h1.hidden LinkNote
    div.kv-warpper
        main
            section.slogan
                h2 Collaborate with partners
                p Build your second brain
                p with <span id='code'>CODE</span> method.
                div.words
                    p.code <span class="initials">C</span>pature
                    p.code <span class="initials">O</span>rganization
                    p.code <span class="initials">D</span>istill
                    p.code <span class="initials">E</span>xpress
            section.indexForm
                <HomePageSignInForm />
</template>
<script lang="ts" setup>
import "~/assets/unity/base.css";

const displaySignInForm = ref(true);
</script>

<style>
.kv-wrapper {
  max-width: 1200px;
  height: 100vh;
  margin: auto;
  padding: 20px;
}

main {
  width: 100vw;
  display: flex;
  margin: auto;
  gap: 150px;
}

.initials {
  color: green;
  font-size: 32px;
  font-weight: 600;
}

.code {
}
</style>
