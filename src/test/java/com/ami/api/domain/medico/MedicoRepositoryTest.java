package com.ami.api.domain.medico;

import jakarta.persistence.EntityManager;
import com.ami.api.domain.consulta.Consulta;
import com.ami.api.domain.direccion.DatosDireccion;
import com.ami.api.domain.paciente.DatosRegistroPacienteDTO;
import com.ami.api.domain.paciente.Paciente;
import com.ami.api.domain.usuario.Rol;
import com.ami.api.domain.usuario.RolRepository;
import com.ami.api.domain.usuario.Usuario;
import com.ami.api.domain.usuario.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Debería devolver null cuando el médico existe pero no está disponible en esa fecha")
    void elegirMedicoAleatorioDisponibleEnLaFechaEscenario1() {
        var lunesSiguienteALas10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);

        var medico = registrarMedico("Medico 1", "medico@gmail.com", "123456", Especialidad.DERMATOLOGIA);
        var paciente = registrarPaciente("Paciente 1", "paciente@gmail.com", "123456789");
        registrarConsulta(medico, paciente, lunesSiguienteALas10);

        var medicoLibre = medicoRepository.elegirMedicoAleatorioDisponibleEnLaFecha(
                Especialidad.DERMATOLOGIA, lunesSiguienteALas10);

        assertThat(medicoLibre).isNull();
    }

    @Test
    @DisplayName("Debería devolver médico cuando está disponible en esa fecha")
    void elegirMedicoAleatorioDisponibleEnLaFechaEscenario2() {
        var lunesSiguienteALas10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);

        var medico = registrarMedico("Medico 1", "medico@gmail.com", "123456", Especialidad.DERMATOLOGIA);

        var medicoLibre = medicoRepository.elegirMedicoAleatorioDisponibleEnLaFecha(
                Especialidad.DERMATOLOGIA, lunesSiguienteALas10);

        assertThat(medicoLibre).isEqualTo(medico);
    }

    // ---------- Helpers ----------

    private void registrarConsulta(Medico medico, Paciente paciente, LocalDateTime fecha) {
        entityManager.persist(new Consulta(null, medico, paciente, fecha, null));
    }

    private Medico registrarMedico(String nombre, String email, String documento, Especialidad especialidad) {
        Usuario usuario = registrarUsuario(email, "123456", "ROLE_MEDICO");
        var medico = new Medico(datosMedico(nombre, email, documento, especialidad), usuario);
        entityManager.persist(medico);
        return medico;
    }

    private Paciente registrarPaciente(String nombre, String email, String documento) {
        Usuario usuario = registrarUsuario(email, "123456", "ROLE_PACIENTE");
        var paciente = new Paciente(datosPaciente(nombre, email, documento), usuario);
        entityManager.persist(paciente);
        return paciente;
    }

    private Usuario registrarUsuario(String email, String password, String rolNombre) {
        Rol rol = rolRepository.findByNombre(rolNombre)
                .orElseGet(() -> {
                    Rol nuevoRol = new Rol();
                    nuevoRol.setNombre(rolNombre);
                    return rolRepository.save(nuevoRol);
                });

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.getRoles().add(rol);

        return usuarioRepository.save(usuario);
    }

    private DatosRegistroMedico datosMedico(String nombre, String email, String documento, Especialidad especialidad) {
        return new DatosRegistroMedico(
                nombre,
                email,
                "23344344",
                documento,
                especialidad,
                datosDireccion(),
                "123456" // password para el usuario
        );
    }

    private DatosRegistroPacienteDTO datosPaciente(String nombre, String email, String documento) {
        return new DatosRegistroPacienteDTO(
                nombre,
                email,
                "1234564445",
                documento,
                datosDireccion(),
                "123456" // password para el usuario
        );
    }

    private DatosDireccion datosDireccion() {
        return new DatosDireccion(
                "calle x",
                "distrito y",
                "ciudad z",
                "1234",
                "1",
                "bogota",
                "suba"
        );
    }
}
