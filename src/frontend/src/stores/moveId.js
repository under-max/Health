import {createStore} from 'vuex'
import {ref} from "vue";

export default createStore({
    state: ref({
        selectedValues: {
            selectedCenterId: "",
            selectedTrainerId: ""
        }
    }),
    mutations: {
        setSelectedValues(state, values) {
            state.selectedValues = values;
        }
    },
    getters: {
        getSelectedValues(state) {
            return state.value.selectedValues;
        }
    }
});