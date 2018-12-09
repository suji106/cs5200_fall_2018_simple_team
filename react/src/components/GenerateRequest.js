import React from 'react';

import RequestService from '../services/RequestService'

class GenerateRequest extends React.Component {
    constructor() {
        super();
        //this.MovieService = MovieService.instance;
        this.messageChanged = this.messageChanged.bind(this);
        this.createRequest = this.createRequest.bind(this);
        this.RequestService = RequestService.instance;
        this.state = {
            message: ""
        }
    }

    createRequest() {
        this.RequestService.createRequest(this.props.movieId, {message: this.state.message}).then(
            () => {
                alert("request sent");
                window.location.reload()
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
            <div className=" app-container1">
                <div className="input-group mb-3">
                    <div className="input-group ">
                        <div className="input-group-prepend">
                            <span className="input-group-text">Your Role</span>
                        </div>
                        <textarea onChange={this.messageChanged} placeholder="How can you help us with this movie?"
                                  className="form-control" aria-label="With textarea"></textarea>
                    </div>
                    <button type="button" className="btn btn-success" onClick={this.createRequest}>Send Request</button>
                </div>
            </div>
        )
    }
}

export default GenerateRequest;
