let _singleton = Symbol();

class MeetingService {
    constructor(singletonToken) {
        if (_singleton !== singletonToken)
            throw new Error('Cannot instantiate directly.');
    }

    static get instance() {
        if (!this[_singleton])
            this[_singleton] = new MeetingService(_singleton);
        return this[_singleton]
    }

    getMeetings(movieId) {
        return fetch('https://moviewalk.herokuapp.com/api/' + movieId + '/meetings', {
            credentials: 'include'
        }).then(response => response.json());


    }

    deleteMeeting(meetingId){

        return fetch('https://moviewalk.herokuapp.com/api/meeting/'+meetingId,{
            method:'delete',
            credentials: 'include'});
    }

}
export default MeetingService;
