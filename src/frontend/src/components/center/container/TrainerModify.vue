<template>
  <div class="black-bg">
    <div class="white-bg">


      <div class="result_modify_bg" v-if="result_modify_modal">
        <div class="result_modify">
          <div v-if="centerSearchResult">
            <h3>center검색</h3>
            <div v-for="(data, index) in centerSearchResult" :key="index">
              <h5 @click="TrainerResultModifyData(data.name)">{{data.name}}</h5>
            </div>
          </div>
          
          <div v-if="TrainerResultData">
            <div v-for="(data, index) in TrainerResultData" :key="index">
              <h3>트레이너를 선택해 주세요</h3>
              <h5 @click="modifyTrainerSelect(data.memberId)">{{data.name}}</h5>
            </div>
          </div>
          

          <hr>
          <button @click="result_modify_modal = false">닫기</button>
        </div>
      </div>


      <h2>트레이너 정보 수정</h2>
      <form>
        <!-- 트레이너 이름으로 검색 후 centerId 확인 centerId(검색), name(보여주기) -->

        <label for="modifySearch">센터검색</label>
        <input
          id="modifySearch"
          type="text"
          v-model="modifyTrainerName"
          @keydown.enter="modifyTrainerSearch"
        />
        <button @click="modifyTrainerSearch">검색</button>
      </form>

      <form action="" enctype="multipart/form-data">
        <!-- Join으로 3개 테이블 축약 후 데이터 리턴  -->
        <div v-if="selectTrainerResult.length !== 0">          
            <h4>센터명:{{ selectTrainerResult.centerName }}</h4>          
            <h4>트레이너명:{{ selectTrainerResult.name }}</h4>   
            <h4>{{ selectTrainerResult.info }}</h4>   
            
            <label for="info">소개: </label>
            <input id="info" type="text" v-model="selectTrainerResult.info" @change="inputChange"/>
            <div>
              <h4>사진</h4>
              <img :src="`https://bucket0503-1000011011001.s3.ap-northeast-2.amazonaws.com/health/${selectTrainerResult.centerId}/${selectTrainerResult.id}/${selectTrainerResult.fileName}`" alt="사진">          
              <input type="checkbox" :value="selectTrainerResult.fileName" v-model="fileName"/>
              
              <br>
              <label for="addFile">+</label>
              <input id="addFile" type="file" accept="image/*" @change="trainerImage"/><br>
            </div> 
        </div>
        

        <hr />
        <button @click="TrainerModifySubmitBtn">수정</button>
        <button @click="TrainerDeleteSubmitBtn">삭제</button>
      </form>
      <hr />
      <button @click="closeModal">닫기</button>
    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits, defineProps, watch } from "vue";
import axios from "axios";
//Modal props 설정
const props = defineProps({
  trainerModifyModal: Boolean,
});

const emit = defineEmits(["closeTrainerModifyModal"]);

const closeModal = () => {
  emit("closeTrainerModifyModal");
};

const result_modify_modal = ref(false);

//url
const url = "http://localhost:8090/";

// 센터검색
const modifyTrainerName = ref("");
// 센터이름검색결과
const centerSearchResult = ref([]);

// 센터 혹은 트레이너 이름으로 검색 get
const modifyTrainerSearch = async (e) => {
  e.preventDefault();  
  try {
    const response = await axios.get(url+"center/searchCenterName", {
      params: {
        searchName: modifyTrainerName.value,
      },
    });    
    centerSearchResult.value = response.data;
    console.log(centerSearchResult.value);
    result_modify_modal.value = true;
    
  } catch (error) {
    console.log(error);
  }
};

//선택한 센터 id 저장
const TrainerModifyCenterName = ref(0);

//axios로 받아올 데이터 저장
const TrainerResultData = ref();

// 센터이름 누르면 실행 
const TrainerResultModifyData = async(centerName) => {   
  TrainerModifyCenterName.value = centerName;  

  try{
    const response = await axios.get(url+"center/modifyTrainerSearch",{
      params: {
        centerName: TrainerModifyCenterName.value,
      }
    });    
    TrainerResultData.value = response.data;
    console.log(TrainerResultData.value);
    centerSearchResult.value = null;
  }catch(error){
    console.log(error);
  }
}

const selectTrainerResult = ref([]);
//선택 centerId 구분
const modifyTrainerSelect = (memberId) => {  
  result_modify_modal.value = false;  
  TrainerResultData.value.map(data=>{    
    if(data.memberId === memberId){
      selectTrainerResult.value = data;           
    }
  })  
}

//트레이너 삭제 전송 버튼
const TrainerDeleteSubmitBtn = async(e) => {
  e.preventDefault();

  try{
    await axios.delete(url+"center/deleteTrainer",{data:selectTrainerResult.value});
    console.log("성공");
  }catch(error){
    console.log(error);
  }


}


// modify 처리
//파일 지우는지 확인 여부
const fileName = ref();
const removeFileName = ref('');
//파일 처리 로직

const trainerModifyImg = ref([]);
const trainerImage = (e) => {
  const files = e.target.files;
  for(let i = 0; i < files.length; i++){
    trainerModifyImg.value.push(files[i]);
  }
}

//modify 전송버튼
const TrainerModifySubmitBtn = async(e) => {
  e.preventDefault();

  if(fileName.value === true){
    removeFileName.value = selectTrainerResult.value.fileName;
  }

  const CenterTrainerModifyData = new FormData();
  for (let i = 0; i < trainerModifyImg.value.length; i++) {
    CenterTrainerModifyData.append("trainerImg", trainerModifyImg.value[i]);
  }

  CenterTrainerModifyData.append("removeImg", removeFileName.value);
  CenterTrainerModifyData.append("trainerId", selectTrainerResult.value.id);
  CenterTrainerModifyData.append("modifyInfo", selectTrainerResult.value.info);
  CenterTrainerModifyData.append("centerId", selectTrainerResult.value.centerId);  
  
  try{
    await axios.put(url+"center/trainerModify",CenterTrainerModifyData,{
            headers:{
                "Content-Type": "multipart/form-data",
            }
        }); 
  }catch(error){
    console.log(error);
  }
}

</script>

<style scoped>
.result_modify_bg {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 998;
  background-color: rgba(0, 0, 0, 0.4);
}
.result_modify {
  z-index: 999;
  position: absolute;
  width: 50%;
  height: 50%;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  padding: 20px;
  border-radius: 30px;
}
.black-bg {
  width: 90%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  position: fixed;
  padding: 20px;
}
.white-bg {
  width: 100%;
  background: white;
  border-radius: 30px;
  padding: 20px;

  max-height:85%;
  overflow-y: auto;
}
</style>