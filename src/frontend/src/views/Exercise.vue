<template>
  <div v-if="isLoading">
    <div class="loader">
      <div class="spinner-border text-light" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>
  </div>
  <div v-else>
    <div class="col-12 col-lg-10 centered">
      <div class="card">
        <h2>고객리스트</h2>

        <table class="table">
          <thead>
          <tr class="d-flex justify-content-between">
            <th>이름</th>
            <th>생년월일</th>
            <th>담당 트레이너</th>
            <th>이용 기간</th>
            <th>남은 PT 횟수</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="user in showAllUsers ? users : users.slice(0, visibleUserCount)" :key="user.id"
              class="d-flex justify-content-between">
            <td>
              <div>
                <router-link :to="{ name: 'userDetail', params: { userId: user.id } }">
                  {{ user.name }}
                </router-link>
              </div>
            </td>
            <td>{{ user.birthDate }}</td>
            <td v-if="user.trainerName != null">{{ user.trainerName }}</td>
            <td v-else>트레이너가 없습니다.</td>
            <td v-if="user.startDate != null">{{ user.startDate }} ~ {{ user.endDate }}</td>
            <td v-else>이용권이 없습니다.</td>
            <td v-if="user.remainingPT != null">{{ user.remainingPT }}</td>
            <td v-else>PT횟수가 없습니다.</td>
          </tr>
          </tbody>
        </table>
        <button v-if="authority === 2" type="button" class="btn btn-primary" @click="toggleShowAllUsers">{{
            showAllUsers ? '줄이기' : '더보기'
          }}
        </button>
      </div>
      <br>
    </div>

    <div class="col-12 col-lg-10 col-lg-6 centered">
      <div class="card">
        <h2>트레이너</h2>
        <table class="table">
          <thead>
          <tr>
            <th>이름</th>
            <th>별명</th>
            <th>센터 이름</th>
            <th>센터 주소</th>
            <th>정보</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="trainer in trainers" :key="trainer.id">
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
            <td>{{trainer.trainerNickName}}</td>
            <td>{{ trainer.centerName }}</td>
            <td>{{ trainer.centerAddress }}</td>
            <td>{{ trainer.trainerInfo }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  <br>

  <div class="col-12 col-lg-10 col-lg-6 centered">
    <div class="card">
      <h2>센터</h2>
      <table class="table">
        <thead>
        <tr>
          <th>센터 이름</th>
          <th>주소</th>
          <th>정보</th>
          <th>별점</th>
          <th>전화 번호</th>
        </tr>
        </thead>
        <tbody>
        <tr>
          <div>
            <a :href="`/center/${center.id}`">
              <td>{{ center.name }}</td>
            </a>
          </div>
          <td>{{ center.address }}</td>
          <td>{{ center.info }}</td>
          <td v-if="center.star !== null">{{ center.star }}</td>
          <td v-else>0</td>
          <td>{{ center.phoneNumber }}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
  <br>

</template>

<script setup>
import {onMounted, ref, computed} from 'vue'
import axios from 'axios'
import Cookies from 'vue-cookies'

const users = ref([])
const trainers = ref([])
const authority = ref('')
const isLoading = ref(true)
const center = ref({})
const visibleUserCount = ref(5);
const showAllUsers = ref(false);
const showMoreUsers = () => {
  visibleUserCount.value += 5;
};
const toggleShowAllUsers = () => {
  showAllUsers.value = !showAllUsers.value;
  if (!showAllUsers.value) {
    visibleUserCount.value = 5;
  }
};

onMounted(async () => {
  setTimeout(() => {
    isLoading.value = false;
  }, 200);
  try {
    const token = Cookies.get('accessToken')
    const [authorityResponse, trainersResponse, usersResponse, centerResponse] = await Promise.all([
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
      }),
      // 여기 수정
      axios.get('/api/user/getCenter', {
        headers: {
          Authorization: token
        }
      })
    ])
    authority.value = authorityResponse.data
    trainers.value = trainersResponse.data
    users.value = usersResponse.data
    center.value = centerResponse.data
    console.log("center", centerResponse.data)
  } catch (error) {
    alert(error.response.data.message)
  }
})
</script>

<style scoped>
button {
  margin-left: auto;
  margin-right: auto;
  max-width: 200px;
  padding: 8px 16px;
}

.centered {
  justify-content: center;
  align-items: center;
  margin-left: auto;
  margin-right: auto;
}

.select-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10px;
}

.select-wrapper select {
  width: 150px;
  padding: 5px;
  font-size: 14px;
}

th {
  border-bottom: none;
}

card {
  max-width: 300px;
}

td {
  text-align: left;
  border-bottom: none;
}

.loader {
  position: fixed;
  z-index: 999;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.card {
  padding: 10px;
}

th {
  text-align: left;
}

</style>