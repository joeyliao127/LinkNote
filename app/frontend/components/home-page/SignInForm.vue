<template>
  <div class="signin-ctn blurry-background">
    <div class="form-ctn flex">
      <p id="login-text" class="signin-ctn">SignIn</p>
      <div class="email flex input-box">
        <input type="email" v-model="email" id="signin-email" />
        <div class="icon">
          <img src="~/assets/img/email.png" alt="email" />
        </div>
      </div>
      <div class="password flex input-box">
        <input
          type="password"
          v-model="password"
          name="password"
          id="signin-password"
        />
        <div class="icon">
          <img src="~/assets/img/password.png" alt="password" />
        </div>
      </div>
      <p id="idsignin-error-msg"></p>
      <button @click="signIn">SignIn</button>

      <div class="switch-form">
        <p class="text-light-gray">
          Don't have an account? Click
          <span class="text-[#778b86] cursor-pointer" @click="switchFormStatus"
            >Register
          </span>
        </p>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
const emit = defineEmits(["switchFormStatus"]);
const authURL = inject("authURL");
const email = ref("test@test.com");
const password = ref("abc123");

async function signIn() {
  try {
    const res = await $fetch(`${authURL}/api/auth/user/signin`, {
      method: "POST",
      body: {
        email: email.value,
        password: password.value,
      },
    });

    console.log(res);
  } catch (e) {
    console.log(e);
  }
}

function switchFormStatus() {
  emit("switchFormStatus");
}
</script>

<style>
.blurry-background {
  width: 100%;
  height: 500px;
  flex-shrink: 0;
  right: 20px;
  flex-grow: 0;
  border-radius: 20px;
  background: transparent;
  border: 2px solid rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(20px);
  box-shadow: 0 0 30px rgba(0, 0, 0, 0.2);
  padding: 40px 50px 10px 50px;
}

.blurry-background .form-ctn {
  flex-direction: column;
  gap: 25px;
  .input-box {
    position: relative;
    input {
      width: 100%;
      background: transparent;
      border: none;
      border-bottom: #052940 2px solid;
      position: relative;
      padding: 0 5px;
      color: white;
      &::placeholder {
        padding: 0 5px;
        color: #dddddd;
      }
      &:focus {
        outline: none;
      }
    }
    .icon {
      position: absolute;
      top: -10px;
      right: 10px;
    }
  }

  img {
    width: 25px;
    height: 25px;
  }
  #login-text {
    text-align: center;
    font-size: 30px;
    font-weight: 500;
    color: #263935;
  }
  button {
    width: 100%;
    height: 40px;
    margin: 10px auto 0 auto;
    border-radius: 12px;
    border: 1px solid #263935;
    background-color: #263935;
    color: white;
    cursor: pointer;
    transition: all 0.2s ease-in-out;
    &:hover {
      opacity: 0.8;
    }
  }
}
</style>
