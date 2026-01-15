
// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.vo.EntityVO;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;
import java.math.BigDecimal;

import com.sankhya.util.TimeUtils;

public class EventoChamadoTI implements EventoProgramavelJava {

   public void afterDelete(PersistenceEvent event) throws Exception {
   }

   public void afterInsert(PersistenceEvent event) throws Exception {
      this.enviaEmailChamadosTI(event);

   }

   public void afterUpdate(PersistenceEvent event) throws Exception {
   }

   public void beforeCommit(TransactionContext tranCtx) throws Exception {

      
   }

   public void beforeDelete(PersistenceEvent event) throws Exception {
   }

   public void beforeInsert(PersistenceEvent event) throws Exception {
   }

   public void beforeUpdate(PersistenceEvent event) throws Exception {
   }

  
   private void enviaEmailChamadosTI(PersistenceEvent event) throws Exception {
      DynamicVO voChamado = (DynamicVO) event.getVo();
      // usuario logado
      // colunas da tela principal
      BigDecimal codChamado = voChamado.asBigDecimal("NUMCHAM");
      String descricao = voChamado.asString("CHDESC");
      String status = 
                  voChamado.asString("STATUS").equals("A") ? "Aberto" : null;
      String prioridade = 
                  voChamado.asString("CHPRIO").equals("B") ? "Baixa" :
                  voChamado.asString("CHPRIO").equals("N") ? "Normal" :
                  voChamado.asString("CHPRIO").equals("U") ? "Urgente" : null;


      String assunto = "Novo Chamado TI - Nº " + codChamado;
      // emails destinatários
      String emailTI = "ti@damezza.com.br";

      String mensagem = "Olá,\n\n" +
            "Um novo chamado de TI foi aberto.\n\n" +
            "Número: " + codChamado + "\n" +
            "Status: " + status + "\n" +
            "Prioridade: " + prioridade + "\n\n" +
            "Descrição:\n" + descricao;

      JapeSession.SessionHandle hnd = null;

      try {
         hnd = JapeSession.open();
         EntityFacade dwfFacade = EntityFacadeFactory.getDWFFacade();

         EntityVO entityVO = dwfFacade.getDefaultValueObjectInstance("MSDFilaMensagem");

         if (voChamado.asString("TIPCH").equals("A")) {

            DynamicVO dynamicVO = (DynamicVO) entityVO;
            dynamicVO.setProperty("ASSUNTO", assunto);
            dynamicVO.setProperty("DTENTRADA", TimeUtils.getNow());
            dynamicVO.setProperty("STATUS", "Pendente");
            dynamicVO.setProperty("EMAIL", emailTI);
            dynamicVO.setProperty("TENTENVIO", BigDecimal.ONE);
            dynamicVO.setProperty("MENSAGEM", mensagem.toCharArray());
            dynamicVO.setProperty("TIPOENVIO", "E");
            dynamicVO.setProperty("MAXTENTENVIO", BigDecimal.valueOf(3L));
            // refencia o SMTP configurado no sistema para envio de emails
            // damezza@damezza.com.br
            dynamicVO.setProperty("CODSMTP", BigDecimal.valueOf(1l));
            dynamicVO.setProperty("CODCON", BigDecimal.ZERO);
            dwfFacade.createEntity("MSDFilaMensagem", entityVO);
         }

         if (voChamado.asString("TIPCH").equals("B") || voChamado.asString("TIPCH").equals("S")) {

            DynamicVO dynamicVO = (DynamicVO) entityVO;
            dynamicVO.setProperty("ASSUNTO", assunto);
            dynamicVO.setProperty("DTENTRADA", TimeUtils.getNow());
            dynamicVO.setProperty("STATUS", "Pendente");
            dynamicVO.setProperty("EMAIL", emailTI);
            dynamicVO.setProperty("TENTENVIO", BigDecimal.ONE);
            dynamicVO.setProperty("MENSAGEM", mensagem.toCharArray());
            dynamicVO.setProperty("TIPOENVIO", "E");
            dynamicVO.setProperty("MAXTENTENVIO", BigDecimal.valueOf(3L));
            dynamicVO.setProperty("CODSMTP", BigDecimal.valueOf(1l));
            dynamicVO.setProperty("CODCON", BigDecimal.ZERO);
            dwfFacade.createEntity("MSDFilaMensagem", entityVO);
         }

      } finally {
         JapeSession.close(hnd);
      }

   }

   
}
