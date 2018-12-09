let _singleton = Symbol();
const REQUEST_API_URL =
    'https://moviewalk.herokuapp.com/api/request';

class RequestService {
    constructor(singletonToken) {
        if (_singleton !== singletonToken)
            throw new Error('Cannot instantiate directly.');
    }
    static get instance() {
        if(!this[_singleton])
            this[_singleton] = new RequestService(_singleton);
        return this[_singleton]
    }

    createRequest(movieId, request) {
        return fetch("https://moviewalk.herokuapp.com/api/" + movieId + "/request", {
            body: JSON.stringify(request),
            headers: {
                'Content-Type': 'application/json'
            },credentials: 'include',
            method: 'POST'
        }).then(function (response) {
            return response.json();
        })}


    getRequestStatus(movieId){
        return fetch('https://moviewalk.herokuapp.com/api/'+movieId+'/request',{
            credentials: 'include'}).then(response => response.json());
    }


    getContriRequests(movieId){

        return fetch('https://moviewalk.herokuapp.com/api/'+movieId+'/requests/contributors/pending',{
            credentials: 'include'}).then(response => response.json());
    }


    getCriticRequests(movieId){

        return fetch('https://moviewalk.herokuapp.com/api/'+movieId+'/requests/mentors/pending',{

            credentials: 'include'}).then(response => response.json());
    }

    getViewers(movieId){

        return fetch('https://moviewalk.herokuapp.com/api/'+movieId+'/requests/contributors/accepted',{

            credentials: 'include'}).then(response => response.json());
    }
    getCritics(movieId){

        return fetch('https://moviewalk.herokuapp.com/api/'+movieId+'/requests/mentors/accepted',{

            credentials: 'include'}).then(response => response.json());
    }


    acceptRequest(requestId){

        return fetch('https://moviewalk.herokuapp.com/api/request/accepted/'+requestId,{
            method:'PUT',
            credentials: 'include'}).then(response => response.json());
    }

    rejectRequest(requestId){

        return fetch('https://moviewalk.herokuapp.com/api/request/rejected/'+requestId,{
            method:'PUT',
            credentials: 'include'}).then(response => response.json());
    }

    deleteRequest(requestId){

        return fetch('https://moviewalk.herokuapp.com/api/request/'+requestId,{
            method:'delete',
            credentials: 'include'});
    }

}
export default RequestService;
