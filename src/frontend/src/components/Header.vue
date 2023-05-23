<template>
<header class="navBars">
    <nav class="navBar">
      <div class="desktop">
        <ul class="no_dot">
          <li>
            <RouterLink to="/">
              <div class="logo">
                <img :src="logo" alt="LogoBar">
              </div>
            </RouterLink>
          </li>

          <li>
            <button>
              <RouterLink to="/exercise">운동관리</RouterLink>
            </button>
          </li>

          <li>
            <button>
              <RouterLink to="/search">지점검색</RouterLink>
            </button>
          </li>

          <li>
            <button>
              <RouterLink to="/membership">이용권 안내</RouterLink>
            </button>
          </li>

          <li>
            <button>
              <RouterLink to="/community">커뮤니티</RouterLink>
            </button>
          </li>

          <li>
            <template v-if="!Login">
              <button>
                <RouterLink to="/login">Login</RouterLink>
              </button>
            </template>
            <template v-else>
              <button>
                <RouterLink to="/info">MyPage</RouterLink>
              </button>
            </template>
          </li>

        </ul>
      </div>
    </nav>   
  </header>
</template>

<script setup>
import logo from '@/assets/Logo.png';
</script>

<style scoped>
.navBars{
  height: 50px;
  width: 100%;
  margin-bottom: 0px;
  
}
.navBar {
  position: fixed;
  z-index: 99;
  top: 0;
  width: 100%;  
  border: 2px, solid, black;
}

.no_dot {
  list-style-type: none;
  display: flex;  
  justify-content: space-around;
  
  margin: 0px;
}

.no_dot a{
  text-decoration: none;
  color: white;   
  font-size: 21px;
  
}

.no_dot button {
  background-color: #112470;
  border: none;
  border-radius: 50px;  
}

.no_dot button:hover{
  background-color: rgba(255, 255, 255, 0.3);
  padding: 10px 30px;
}

.no_dot li:not(:first-of-type){
  margin-top: 15px;
  margin-bottom: 15px;

}

.no_dot li:first-child {
  margin-top: 10px;
}

</style>