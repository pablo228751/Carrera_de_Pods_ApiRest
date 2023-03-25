/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.com.pablofelice.carreraDePods.mapper;

/**
 *
 * @author Usuario
 */

public interface IMapper<In, Out> {
    Out mapToEntity(In dto);
    In mapToDTO(Out entity);
}
