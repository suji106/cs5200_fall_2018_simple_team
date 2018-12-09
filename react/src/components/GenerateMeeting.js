import React from 'react';



class GenerateMeeting extends React.Component {
    constructor() {
        super();
        //this.MovieService = MovieService.instance;
        this.titleChanged = this.titleChanged.bind(this);
        this.messageChanged = this.messageChanged.bind(this);
        this.createMeeting = this.createMeeting.bind(this);
        this.locationChanged = this.locationChanged.bind(this);
        this.dateChanged = this.dateChanged.bind(this);
        this.state ={
            title:"",
            message:"",
            location:"",
            meetingDate:""
        }


    }


    createMeeting(){

        let meeting ={
            title:this.state.title,
            message:this.state.message,
            location:this.state.location,
            time:this.state.meetingDate
        }
        fetch("https://moviewalk.herokuapp.com/api/"+this.props.movieId +"/meeting", {
            body: JSON.stringify(meeting),
            headers: {
                'Content-Type': 'application/json'
            },
            method: 'POST',
            credentials:"include"
        }).then(function (response) {
            return response.json();
        }).then(response => {alert("meeting added");
        window.location.reload()});





    }

    dateChanged(event) {
        this.setState({
            meetingDate:
            event.target.value

        });
    }

    locationChanged(event) {
        this.setState({
            location:
            event.target.value

        });
    }


    titleChanged(event) {
        this.setState({
            title:
            event.target.value

        });
    }


    messageChanged(event) {
        this.setState({
            message:
            event.target.value

        });
    }

    render() {
        return (
            <div className="app-container1">

                <div className="input-group mb-3">

                    <div className="input-group-prepend">
                        <span className="input-group-text" id="">Meeting title</span>
                    </div>
                    <input onChange={this.titleChanged} type="text" className="form-control" placeholder="enter the title of the meeting"
                    />

                </div >

                <div className="input-group mb-3">

                    <div className="input-group-prepend">
                        <span className="input-group-text" id="">Meeting Location</span>
                    </div>
                    <input onChange={this.locationChanged} type="text" className="form-control" placeholder="enter the meeting location"
                    />

                </div >


                <div className="input-group ">
                    <div className="input-group-prepend">
                        <span className="input-group-text">Meeting Time</span>
                    </div>
                    <input type="datetime-local" onChange={this.dateChanged}/>
                </div>

                <div className="input-group ">
                    <div className="input-group-prepend">
                        <span className="input-group-text">Meeting Agenda </span>
                    </div>
                    <textarea onChange={this.messageChanged} className="form-control" aria-label="With textarea"></textarea>
                </div>

                <button type="button" className="btn btn-success btn-block"  onClick={this.createMeeting}>Add Meeting</button>

            </div>



        )
    }
}
export default GenerateMeeting;
