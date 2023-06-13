<template>
  <hr>
  <h1>운동관리!</h1>
  <hr>

    <h1>고객리스트</h1>
    <table class="card">
      <thead class="card-header">
      <tr>
        <th>#</th>
        <th>이름</th>
        <th>생년월일</th>
        <th>담당 트레이너</th>
        <th>이용 기간</th>
        <th>남은 PT 횟수</th>
        <th>현재 상태</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="user in users" :key="user.id">
        <td>{{ user.id }}</td>
        <td>
          <div>
            <router-link :to="{ name: 'userDetail', params: { userId: user.id } }">
              {{ user.name }}
            </router-link>
          </div>
        </td>
        <td>{{ user.birthDate }}</td>
        <td>{{ user.trainerName }}</td>
        <td>{{user.startDate}} ~ {{user.endDate}}</td>
        <td>{{ user.remainingPT }}</td>
        <td>{{ user.isInCenter }}</td>
      </tr>
      </tbody>
    </table>
    <hr>

    <h1>트레이너</h1>
    <table class="table">
      <thead>
      <tr>
        <th>#</th>
        <th>이름</th>
        <th>센터 이름</th>
        <th>센터 주소</th>
        <th>정보</th>

      </tr>
      </thead>
      <tbody>
      <tr v-for="trainer in trainers" :key="trainer.id">
        <td>{{ trainer.id }}</td>
        <td>
          <div v-if="authority === 2">
            <router-link :to="{ name: 'trainerCalendar', params: { trainerId: trainer.id } }">
              {{ trainer.name }}
            </router-link>
          </div>
          <div v-if="authority === 1">
            <router-link :to="{ name: 'trainerDetail', params: { trainerId: trainer.id } }">
              {{ trainer.name }}
            </router-link>
          </div>
          <div >

          </div>
        </td>
        <td>{{ trainer.centerName }}</td>
        <td>{{ trainer.centerAddress }}</td>
        <td>{{ trainer.trainerInfo }}</td>
      </tr>

      </tbody>

    </table>


</template>

<script setup>
import {onMounted, ref} from 'vue'
import axios from "axios";
import Cookies from "vue-cookies";

const id = ref();
const name = ref();
const users = ref([])
const trainers = ref([]);
const authority = ref('');

onMounted(() => {
  const token = Cookies.get("accessToken")
  axios.post("/api/user/getAuthority", {}, {
    headers : {
      Authorization : token
    }
  })
      .then((response) => {
        authority.value = response.data
        console.log("권한",response.data)
      })
      .catch((error) => {
        alert(error.response.data.message)
      })
})

onMounted(() => {
  const token = Cookies.get("accessToken")
  axios.get("/api/authTrainer", {
    headers: {
      Authorization: token
    }
  })
      .then((response) => {
        console.log("트레이너 정보",response.data);
        trainers.value = response.data
      }).catch((error) => {
    console.log(error);
  });
});

onMounted(() => {
  const token = Cookies.get("accessToken")
  axios.get("/api/authUser", {
    headers: {
      Authorization: token
    }
  })
      .then((response) => {
        console.log("고객리스트", response.data);
        users.value = response.data
      }).catch((error) => {
    console.log(error)
  });
});


</script>

<style scoped>
.table {
  color: white;
}

h1 {
  color : white;
}

</style>