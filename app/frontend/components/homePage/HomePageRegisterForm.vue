<template>
  <div class="signup-ctn blurry-background display-none">
    <div class="form-ctn flex">
      <p id="login-text">SignUp</p>
      <div class="username flex input-box">
        <input
          type="text"
          name="username"
          id="username"
          placeholder="username"
          v-model="username"
        />
        <div class="icon">
          <img src="~/assets/img/person-black.png" alt="email" />
        </div>
      </div>
      <div class="email flex input-box">
        <input
          type="email"
          name="email"
          id="signup-email"
          placeholder="Email"
          v-model="email"
        />
        <div class="icon">
          <img src="~/assets/img/email.png" alt="email" />
        </div>
      </div>

      <div class="password flex input-box">
        <input
          type="password"
          name="password"
          id="signup-password"
          placeholder="Password"
          v-model="password"
        />
        <div class="icon">
          <img src="~/assets/img/password.png" alt="password" />
        </div>
      </div>
      <div class="password flex input-box">
        <input
          type="password"
          name="confirmPassword"
          id="confirmPassword"
          placeholder="Confirm Your Password"
          v-model="confirmPassword"
        />
        <div class="icon">
          <img src="~/assets/img/password.png" alt="password" />
        </div>
      </div>
      <p id="signup-error-msg"></p>
      <button @click="registerUser">SignUp</button>

      <div class="switch-form">
        <p>
          Back to <span id="toSignin" @click="switchFormStatus">Sigin</span>
        </p>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
const emit = defineEmits(["switchFormStatus"]);

const username = ref("");
const email = ref("");
const password = ref("");
const confirmPassword = ref("");

function registerUser() {
  
}

function switchFormStatus() {
  console.log("點擊signup，切換到signin");
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

  #signin-error-msg {
    text-align: center;
    color: red;
  }
  #signup-error-msg {
    text-align: center;
    color: rgb(204, 12, 12);
  }

  .switch-form {
    text-align: center;
    p {
      color: #dddddd;
    }
    span {
      cursor: pointer;
      text-decoration: underline;
      color: #778b86;
      &:hover {
        opacity: 0.7;
      }
    }
  }
}
</style>
