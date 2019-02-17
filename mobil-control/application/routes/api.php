<?php

use Illuminate\Http\Request;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::group(['prefix' => 'gps-coordinate'], function(){
    Route::post('create', function (Request $request) {
        $coordinate = new \App\Models\GpsCoordinate();
        $coordinate->fill($request->all());
        $coordinate->save();
    });

    Route::get('', function(){
        $coordinate =  \App\Models\GpsCoordinate::orderBy('id', 'desc')->firstOrFail();

        return $coordinate;
    });
});


Route::group(['prefix' => 'camera-snapshot'], function(){
    Route::post('create', function (Request $request) {
        $snapshot = new \App\Models\CameraSnapshot();
        $snapshot->fill($request->all());
        $snapshot->save();
    });

    Route::get('', function(){
        $snapshot =  \App\Models\CameraSnapshot::orderBy('id', 'desc')->firstOrFail();

        return $snapshot;
    });
});

Route::group(['prefix' => 'command'], function(){
    Route::get('', function(){
        $command = App\Models\Commands::where('status', \App\Enums\CommandStatus::IN_PROGRESS)->orderBy('id', 'desc')->first();

        return $command;
    });

    Route::post('create', function(Request $request){
        $command = new App\Models\Commands();
        $command->fill($request->all());
        $command->save();
    });

    Route::post('complete', function(){
        App\Models\Commands::where('status', \App\Enums\CommandStatus::IN_PROGRESS)->update(['status' => \App\Enums\CommandStatus::COMPLETED]);
    });
});