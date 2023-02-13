import { createSlice } from "@reduxjs/toolkit";



export const userInfoSlice = createSlice({

  name: 'userInfo',
  initialState: {
    user:{},


  },
  reducers: {
    updateUserInfo: (state, {payload}) => {
      console.log('userInfo 수정 액션 호출')
      // console.log(data)
      state.user = payload
    },
    
  }
})


export const userInfoReducers = userInfoSlice.reducer;
export const userInfoActions = userInfoSlice.actions;