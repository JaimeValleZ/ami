package com.ami.api.domain.consulta;

import com.ami.api.domain.ValidacionException;
import com.ami.api.domain.consulta.validaciones.ValidacionCancelarCita24H;
import com.ami.api.domain.consulta.validaciones.ValidacionDeConsultas;
import com.ami.api.domain.medico.DatosListaMedico;
import com.ami.api.domain.medico.Medico;
import com.ami.api.domain.medico.MedicoRepository;
import com.ami.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaDeConsultas {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository repository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired//Al ser una interfaz, spring reconoce todas las clases que implementan esa interfaz
    private List<ValidacionDeConsultas> validaciones;

    @Autowired
    private ValidacionCancelarCita24H validacionCancelarCita24H;


    public DatosDetalleConsulta reservar(DatosReservaConsulta datos) {
        
        if(!pacienteRepository.existsById(datos.idPaciente())){
            throw new ValidacionException("No existe un paciente con el id informado");
        }
        
        if(datos.idMedico() != null && !medicoRepository.existsById(datos.idMedico())){
            throw new ValidacionException("No existe un medico con el id informado");
        }

        //Validaciones
        validaciones.forEach(validacion -> validacion.validar(datos));

        var medico = elegirMedico(datos);
        if(medico == null){
            throw new ValidacionException("No existe un medico dispinibile en ese horario");
        }
        var paciente = pacienteRepository.findById(datos.idPaciente()).get();
        var consulta = new Consulta(null, medico, paciente, datos.fecha(), null);
        repository.save(consulta);

        return new DatosDetalleConsulta(consulta);
    }

    private Medico elegirMedico(DatosReservaConsulta datos) {
        if(datos.idMedico() != null){
            return medicoRepository.getReferenceById(datos.idMedico()); //Reference obtiene el objeto y no un Optional
        }
        if(datos.especialidad() == null){
            throw new ValidacionException("Debe especificar la especialidad cuando no se elije un medico");
        }

        return medicoRepository.elegirMedicoAleatorioDisponibleEnLaFecha(datos.especialidad(), datos.fecha());
    }

    public void cancelar(DatosCancelamientoConsulta datos){
        if(!repository.existsById(datos.idConsulta())){
            throw new ValidacionException("No existe una consulta con el id informado");
        }

        validacionCancelarCita24H.validar(datos);

        var consulta = consultaRepository.getReferenceById(datos.idConsulta());
        consulta.cancelar(datos.motivo());
    }

    public Page<DatosListarConsultas> listarConsultas(Pageable paginacion, Long idPaciente) {
        return consultaRepository.findAllByPacienteId(idPaciente, paginacion).map(DatosListarConsultas::new);
    }
}
