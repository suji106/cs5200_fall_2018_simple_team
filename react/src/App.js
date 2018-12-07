import React, {Component} from 'react';
import logo from './logo.svg';
import './App.css';
import {BrowserRouter as Router, Route} from 'react-router-dom'
import ProjectTiles from './containers/ProjectTiles'
import OwnerProjects from './containers/OwnerProjects'
import Header from './containers/Header'
import Register from './containers/Register'
import ProjectDetail from './containers/ProjectDetail'
import Login from './containers/Login'
import MyProjectsEditor from "./containers/MyProjectsEditor";
import Profile from './containers/Profile';
import UserAdmin from "./containers/UserAdmin";
import MyConnections from "./containers/MyConnections";

class App extends Component {


    render() {

        return (

            <div>
                <Router>


                    <div>
                        <Route path="/"
                               exact component={ProjectTiles}>
                        </Route>
                        <Route path="/projects"
                               exact component={MyProjectsEditor}>
                        </Route>

                        <Route path="/register"
                               exact component={Register}>
                        </Route>

                        <Route path="/login"
                               exact component={Login}>
                        </Route>

                        <Route path="/profile/:id"
                               exact component={Profile}>
                        </Route>

                        <Route path="/ProjectDetail"
                               exact component={ProjectDetail}>
                        </Route>

                        <Route path="/user-admin"
                               exact component={UserAdmin}>
                        </Route>

                        <Route path="/connections"
                               exact component={MyConnections}>
                        </Route>
                    </div>

                </Router>
            </div>

        );
    }
}

export default App;
