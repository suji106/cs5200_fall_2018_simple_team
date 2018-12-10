import React from 'react';

import RequestService from '../services/RequestService'

class GenerateRequest extends React.Component {
    constructor() {
        super();
        //this.MovieService = MovieService.instance;
        this.detailsChanged = this.detailsChanged.bind(this);
        this.createRequest = this.createRequest.bind(this);
        this.RequestService = RequestService.instance;
        this.state = {
            details: ""
        }
    }

    createRequest() {
        this.RequestService.createRequest(this.props.movieId, {details: this.state.details}).then(
            () => {
                alert("request sent");
                // window.location.reload()
            });
    }

    detailsChanged(event) {
        this.setState({
            details:
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
                        <textarea onChange={this.detailsChanged} placeholder="How can you help us with this movie?"
                                  className="form-control" aria-label="With textarea"></textarea>
                    </div>
                    <button type="button" className="btn btn-success" onClick={this.createRequest}>Send Request</button>
                </div>
            </div>
        )
    }
}

export default GenerateRequest;
