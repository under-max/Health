<template>
  <div v-if="isLoading">
    <div class="loader">
      <div class="spinner-border text-light" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>
  </div>
  <div v-else>
    <div class="container-fluid">
      <table class="card">
        <h2>고객리스트</h2>
        <thead class="table-header card-header">
        <tr class="d-flex justify-content-between">
          <th>이름</th>
          <th>생년월일</th>
          <th>담당 트레이너</th>
          <th>이용 기간</th>
          <th>남은 PT 횟수</th>
        </tr>
        </thead>
        <tbody class="table-borderless">
        <tr class="d-flex justify-content-between" v-for="user in users" :key="user.id">
          <td>
            <div>
              <router-link :to="{ name: 'userDetail', params: { userId: user.id } }">
                {{ user.name }}
              </router-link>
            </div>
          </td>
          <td>{{ user.birthDate }}</td>
          <div v-if="user.trainerName != null">
            <td>{{ user.trainerName }}</td>
          </div>
          <td v-else>트레이너가 없습니다.</td>
          <div v-if="user.startDate != null">
            <td>{{ user.startDate }} ~ {{ user.endDate }}</td>
          </div>
          <td v-else>이용권이 없습니다.</td>
          <div v-if="user.remainingPT != null">
            <td>{{ user.remainingPT }}</td>
          </div>
          <td v-else>PT횟수가 없습니다.</td>
        </tr>
        </tbody>
      </table>
    </div>
    <hr>

    <div class="container-fluid">
      <table class="card">
        <h2>트레이너</h2>
        <thead class="table-header card-header">
        <tr class="d-flex justify-content-between">
          <th>이름</th>
          <th>센터 이름</th>
          <th>센터 주소</th>
          <th>정보</th>
        </tr>
        </thead>
        <tbody class="table-borderless">
        <tr class="d-flex justify-content-between" v-for="trainer in trainers" :key="trainer.id">
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
            <div>
            </div>
          </td>
          <td>{{ trainer.centerName }}</td>
          <td>{{ trainer.centerAddress }}</td>
          <td>{{ trainer.trainerInfo }}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import axios from 'axios'
import Cookies from 'vue-cookies'

const users = ref([])
const trainers = ref([])
const authority = ref('')
const isLoading = ref(true)

onMounted(async () => {
  setTimeout(() => {
    isLoading.value = false;
  }, 200);
  try {
    const token = Cookies.get('accessToken')
    const [authorityResponse, trainersResponse, usersResponse] = await Promise.all([
      axios.post('/api/user/getAuthority', {}, {
        headers: {
          Authorization: token
        }
      }),
      axios.get('/api/authTrainer', {
        headers: {
          Authorization: token
        }
      }),
      axios.get('/api/authUser', {
        headers: {
          Authorization: token
        }
      })
    ])

    authority.value = authorityResponse.data
    trainers.value = trainersResponse.data
    users.value = usersResponse.data
  } catch (error) {
    alert(error.response.data.message)
  }
})
</script>

<style scoped>
.loader {
  position: fixed;
  z-index: 999;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.card,
td {
  padding: 10px;
  text-align: left;
}

</style>