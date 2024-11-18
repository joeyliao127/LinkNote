<template>
  <div class="w-screen h-screen">
    <div
      class="flex justify-center w-[100%] m-auto p-[120px] gap-[5vw] pt-[22vh]"
    >
      <section class="slogan flex-col gap-[20px] w-[600px]">
        <h2 class="text-light-gray text-[45px]">Collaborate with partners</h2>
        <p class="text-light-gray text-slogan-content px-[30px] pt-[10px]">
          Build your second brain
        </p>
        <p class="text-light-gray text-slogan-content pl-[180px] pt-[10px]">
          with
          <span class="text-[36px] text-green-700 font-medium">CODE</span>
          method.
        </p>
        <div class="flex-col pl-2 gap-[10px]">
          <p class="code"><span class="initials">C</span>pature</p>
          <p class="code"><span class="initials">O</span>rganization</p>
          <p class="code"><span class="initials">D</span>istill</p>
          <p class="code"><span class="initials">E</span>xpress</p>
        </div>
      </section>
      <section class="index-form-area flex-grow-0 w-[420px]">
        <HomePageSignInForm
          v-if="displaySignInForm"
          @switch-form-status="switchForm"
        />
        <HomePageRegisterForm v-else @switch-form-status="switchForm" />
      </section>
    </div>
  </div>
</template>
<script lang="ts" setup>
import "~/assets/unity/base.css";

const displaySignInForm = ref(true);
const config = useRuntimeConfig();
console.log(config.public);
provide("authURL", config.public.authApiUrl);

const switchForm = () => (displaySignInForm.value = !displaySignInForm.value);
</script>

<style>
.text-slogan-content {
  font-size: 28px;
}

.code {
  color: #f2f2f2;
  font-size: 28px;
  padding-top: 10px;
}

.initials {
  color: green;
  font-weight: 500;
}

.index-form-area {
  width: 420px;
  transition: all 0.2s ease-in-out;
  transition: all 0.2s ease-in-out;
}
</style>
