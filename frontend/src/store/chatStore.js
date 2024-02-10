import { defineStore } from "pinia"
import { useAuthStore } from "./authStore"
import axios from "axios"
import SockJS from "sockjs-client"
import Stomp from 'webstomp-client'
import { createPersistedState } from "pinia-plugin-persistedstate"
import servers from "@/server"

// const server =  'https://i10d109.p.ssafy.io/api'
// const server2 = 'http://localhost:8080/api'
const server = servers
const server2 = 'https://i10d109.p.ssafy.io/api'


export const useChatStore = defineStore('chat',{
    state:()=>({
        stompClient : {},
        message : {},
        countMessage : {},
        notify : [],
        isConnect : false,
        reloadFlag : {},
        nextRoom : {},
    }),
    getters:{
        getStomp : (state)=>{
            return state.stompClient
        },
        getMessage : (state)=>{
            return state.message
        },
        getConnect : (state)=>{
            return state.isConnect
        },
        getNotify : (state)=>{
            return state.notify
        },
        getReload : (state)=>{
            return state.reloadFlag
        },
        getNextRoom : (state)=>{
            return state.nextRoom
        }
    },
    actions:{
        messageRange : function(roomId){
            return this.message[roomId]
        },
        makeChat : async function(idx){
            const auth = useAuthStore()
            await auth.useRefresh()
            const data = {
                "id" : idx
            }
            const headers = {
                'Authorization' : `Bearer ${auth.getAccess}`,
                'Content-Type' : 'application/json'
            }
            console.log('아이디 : ',idx)
            return axios.post(`${server}/chat/personal`,JSON.stringify(data),{headers})
        },
        chatList : async function(){
            const auth = useAuthStore()
            await auth.useRefresh()
            const headers = {
                'Authorization' : `Bearer ${auth.getAccess}`,
                'Content-Type' : 'application/json'
            }
            return axios.get(`${server}/chat/personal`,{ headers })
        },
        userRoom : async function(){
            const auth = useAuthStore()
            const headers = {
                'Authorization' : `Bearer ${auth.getAccess}`,
            }
            return axios.get(`${server}/user/room`,{headers})
        },
        addRoom : function(room){
            this.messsageRoom.push(room)
        },
        setStomp : function(obj){
            this.stompClient = obj
        },
        setMessage : function(roomId,value){
            if( roomId in this.message){
                this.message[roomId].push(value)
                this.countMessage[roomId]+=1

            }else{
                this.message[roomId] = [value]
                this.countMessage[roomId] = 0
            }
        },
        sub : async function(data){
                data.forEach(roomId=>{
                    this.stompClient.subscribe('/sub/'+roomId,(e)=>{
                        this.setMessage(roomId,JSON.parse(e.body))
                    })
                })
        },
        subRequest : async function(data){
            console.log(data)
            this.stompClient.subscribe('/sub/'+data.content,(e)=>{
                this.setMessage(data.content,JSON.parse(e.body))
            })
            // this.stompClient.subscribe('/sub/'+data.)
        },
        makeConnect : async function(data){
            let socket = new SockJS(`${server}/ws-stomp`)
            const headers = {"Authorization": useAuthStore().getAccess}
            this.setStomp(Stomp.over(socket))
            const client = this.getStomp
            await client.connect(headers,()=>{
                this.isConnect = true
            },
            ()=>{
                this.isConnect = false
            })
        },
        notifyConnect : async function(){
            let value = await this.getNotification()
            value.data.forEach(item=>{
                this.notify.push(item)
            })            
            const auth = useAuthStore()
            const userId = auth.getUserInfo.id
            const client = this.getStomp
            client.subscribe(`/sub/`+userId,(e)=>{
                const data = JSON.parse(e.body)
                if(data.dataType == "noti"){
                    this.notify.unshift(data)
                } else if(data.dataType == "sub"){
                    this.subRequest(data)
                }
            })
        },
        getNotification : async function(){
            const auth = useAuthStore()
            const headers = {
                Authorization : `Bearer ${auth.getAccess}`
            }
            return axios.get(`${server}/notification`,{headers})
        },
        readNotification : async function(idx){
            console.log('여기까진 오니?')
            const auth = useAuthStore()
            const headers = {
                Authorization : `Bearer ${auth.getAccess}`,
                'Content-Type' : 'application/json'
            }
            const data = {
                id : idx
            }
            return axios.patch(`${server}/notification/single`,JSON.stringify(data),{headers})
        },
        readMessage : async function(nextUrl){
            const auth = useAuthStore()
            const headers = {
                Authorization : `Bearer ${auth.getAccess}`
            }
            return axios.get(`${servers}/message/scroll${nextUrl}`,{headers})
        }
    },

})