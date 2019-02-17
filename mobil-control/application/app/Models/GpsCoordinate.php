<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class GpsCoordinate extends Model
{
    protected $fillable = ['longitude', 'latitude'];
}
